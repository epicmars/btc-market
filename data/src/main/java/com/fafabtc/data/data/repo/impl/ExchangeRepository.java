package com.fafabtc.data.data.repo.impl;

import android.content.Context;

import com.fafabtc.binance.data.repo.BinanceRepo;
import com.fafabtc.binance.model.BinancePair;
import com.fafabtc.data.data.local.dao.ExchangeDao;
import com.fafabtc.data.data.repo.ExchangeRepo;
import com.fafabtc.data.data.repo.PairRepo;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.entity.exchange.Pair;
import com.fafabtc.data.model.entity.mapper.PairMapperFactory;
import com.fafabtc.gateio.data.repo.GateioRepo;
import com.fafabtc.gateio.model.entity.GateioPair;
import com.fafabtc.huobi.data.repo.HuobiRepo;
import com.fafabtc.huobi.domain.entity.HuobiPair;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class ExchangeRepository implements ExchangeRepo {

    @Inject
    Context context;

    @Inject
    ExchangeDao exchangeDao;

    @Inject
    PairRepo pairRepo;

    @Inject
    GateioRepo gateioRepo;

    @Inject
    BinanceRepo binanceRepo;

    @Inject
    HuobiRepo huobiRepo;

    @Inject
    public ExchangeRepository() {
    }

    @Override
    public Completable init() {
        return Completable.mergeArrayDelayError(
                initGateio(),
                initBinance(),
                initHuobi()
        );
    }

    @Override
    public Completable initGateio() {
        // Initiate exchange and assets data, refresh tickers after exchange and assets initiation have complete.
        return gateioRepo.init()
                .flattenAsObservable(this.<GateioPair>flattenList())
                .map(PairMapperFactory.GateioPairMapper.MAPPER)
                .flatMap(savePair)
                .toList()
                .toCompletable()
                .concatWith(saveExchange(GateioRepo.GATEIO_EXCHANGE));
    }


    @Override
    public Completable initBinance() {
        return binanceRepo.initBinanceData()
                .flattenAsObservable(this.<BinancePair>flattenList())
                .map(PairMapperFactory.BinancePairMapper.MAPPER)
                .flatMap(savePair)
                .toList()
                .toCompletable()
                .concatWith(saveExchange(BinanceRepo.BINANCE_EXCHANGE));
    }

    @Override
    public Completable initHuobi() {
        return huobiRepo.init()
                .flattenAsObservable(this.<HuobiPair>flattenList())
                .map(PairMapperFactory.HuobiPairMapper.MAPPER)
                .flatMap(savePair)
                .toList()
                .toCompletable()
                .concatWith(saveExchange(HuobiRepo.HUOBI_EXCHANGE));
    }

    /**
     * Initiate a exchange and it's trade pairs.
     *
     * @param exchange a exchange
     * @return a Completable
     */
    public Completable init(String exchange) {
        switch (exchange) {
            case GateioRepo.GATEIO_EXCHANGE:
                return initGateio();
            case BinanceRepo.BINANCE_EXCHANGE:
                return initBinance();
            case HuobiRepo.HUOBI_EXCHANGE:
                return initHuobi();
        }
        return Completable.complete();
    }

    private Function<Pair, ObservableSource<Pair>> savePair = new Function<Pair, ObservableSource<Pair>>() {
        @Override
        public ObservableSource<Pair> apply(final Pair pair) throws Exception {
            return pairRepo.save(pair).toSingleDefault(pair).toObservable().onErrorReturnItem(pair);
        }
    };

    @Override
    public Completable saveExchange(final String exchangeName) {
        return Single.fromCallable(new Callable<Exchange>() {
            @Override
            public Exchange call() throws Exception {
                Exchange exchange = new Exchange();
                exchange.setName(exchangeName);
                return exchange;
            }
        }).flatMapCompletable(new Function<Exchange, CompletableSource>() {
            @Override
            public CompletableSource apply(Exchange exchange) throws Exception {
                return save(exchange);
            }
        });
    }

    @Override
    public Completable save(final Exchange exchange) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Exchange cached = exchangeDao.findByName(exchange.getName());
                if (cached == null) {
                    exchangeDao.insertOne(exchange);
                } else {
                    exchange.setId(cached.getId());
                    exchangeDao.updateOne(exchange);
                }
            }
        });
    }

    @Override
    public Single<Exchange[]> getExchanges() {
        return Single.fromCallable(new Callable<Exchange[]>() {
            @Override
            public Exchange[] call() throws Exception {
                return exchangeDao.findAll();
            }
        });
    }

    @Override
    public Single<Exchange> getExchange(final String name) {
        return Single.fromCallable(new Callable<Exchange>() {
            @Override
            public Exchange call() throws Exception {
                return exchangeDao.findByName(name);
            }
        });
    }

    private <T> Function<List<T>, Iterable<T>> flattenList() {
        return new Function<List<T>, Iterable<T>>() {
            @Override
            public Iterable<T> apply(List<T> ts) throws Exception {
                return ts;
            }
        };
    }

}

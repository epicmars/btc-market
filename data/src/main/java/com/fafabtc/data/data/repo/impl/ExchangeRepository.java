package com.fafabtc.data.data.repo.impl;

import android.content.Context;

import com.fafabtc.binance.data.repo.BinanceRepo;
import com.fafabtc.binance.model.BinancePair;
import com.fafabtc.common.file.AndroidAssetsUtils;
import com.fafabtc.common.json.GsonHelper;
import com.fafabtc.data.data.local.dao.ExchangeDao;
import com.fafabtc.data.data.local.dao.PairDao;
import com.fafabtc.data.data.repo.ExchangeRepo;
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
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class ExchangeRepository implements ExchangeRepo {

    private static final String EXCHANGE_FILE_NAME = "exchanges.json";

    @Inject
    Context appContext;

    @Inject
    ExchangeDao dao;

    @Inject
    PairDao pairDao;

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
        return Completable.concatArray(
                initGateio(),
                initBinance(),
                initHuobi()
        );
    }

    @Override
    public Completable initGateio() {
        return gateioRepo.init()
                .flattenAsObservable(this.<GateioPair>flattenList())
                .map(PairMapperFactory.GateioPairMapper.MAPPER)
                .flatMap(savePair)
                .toList()
                .flatMapCompletable(saveExchangeCompletableMap(GateioRepo.GATEIO_EXCHANGE));
    }

    private Function<Pair, ObservableSource<Pair>> savePair = new Function<Pair, ObservableSource<Pair>>() {
        @Override
        public ObservableSource<Pair> apply(final Pair pair) throws Exception {
            return Observable.fromCallable(new Callable<Pair>() {
                @Override
                public Pair call() throws Exception {
                    pairDao.insertOne(pair);
                    return pair;
                }
            }).onErrorReturnItem(pair);
        }
    };

    @Override
    public Completable initBinance() {
        return binanceRepo.initBinanceData()
                .flattenAsObservable(this.<BinancePair>flattenList())
                .map(PairMapperFactory.BinancePairMapper.MAPPER)
                .flatMap(savePair)
                .toList()
                .flatMapCompletable(saveExchangeCompletableMap(BinanceRepo.BINANCE_EXCHANGE));
    }

    @Override
    public Completable initHuobi() {
        return huobiRepo.init()
                .flattenAsObservable(this.<HuobiPair>flattenList())
                .map(PairMapperFactory.HuobiPairMapper.MAPPER)
                .flatMap(savePair)
                .toList()
                .flatMapCompletable(saveExchangeCompletableMap(HuobiRepo.HUOBI_EXCHANGE));
    }

    public Function<List<Pair>, CompletableSource> saveExchangeCompletableMap(final String exchangeName) {
        return new Function<List<Pair>, CompletableSource>() {
            @Override
            public CompletableSource apply(List<Pair> pairList) throws Exception {
                Exchange exchange = new Exchange();
                exchange.setName(exchangeName);
                return save(exchange);
            }
        };
    }

    @Override
    public Completable save(final Exchange exchange) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Exchange cached = dao.findByName(exchange.getName());
                if (cached == null) {
                    dao.insertOne(exchange);
                } else {
                    exchange.setId(cached.getId());
                    dao.updateOne(exchange);
                }
            }
        });
    }

    @Override
    public Single<Exchange[]> getExchanges() {
        return Single.fromCallable(new Callable<Exchange[]>() {
            @Override
            public Exchange[] call() throws Exception {
                return dao.findAll();
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

    private Exchange[] getExchangesFromAssets() {
        String exchangeJSON = AndroidAssetsUtils.readFromAssets(appContext.getAssets(), EXCHANGE_FILE_NAME);
        Exchange[] exchangeArray = GsonHelper.gson().fromJson(exchangeJSON, Exchange[].class);
        return exchangeArray;
    }
}

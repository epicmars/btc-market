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
    public ExchangeRepository() {
    }

    @Override
    public Completable init() {
        return initGateio()
                .concatWith(initBinance());
    }

    @Override
    public Completable initGateio() {
        return gateioRepo.init()
                .flattenAsObservable(new Function<List<GateioPair>, Iterable<GateioPair>>() {
                    @Override
                    public Iterable<GateioPair> apply(List<GateioPair> gateioPairs) throws Exception {
                        return gateioPairs;
                    }
                })
                .map(new Function<GateioPair, Pair>() {
                    @Override
                    public Pair apply(GateioPair gateioPair) throws Exception {
                        return PairMapperFactory.gateioPairMapper.from(gateioPair);
                    }
                })
                .flatMap(savePair)
                .toList()
                .flatMapCompletable(new Function<List<Pair>, CompletableSource>() {
                    @Override
                    public CompletableSource apply(List<Pair> pairList) throws Exception {
                        Exchange exchange = new Exchange();
                        exchange.setName(GateioRepo.GATEIO_EXCHANGE);
                        return save(exchange);
                    }
                });
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
                .flattenAsObservable(new Function<List<BinancePair>, Iterable<BinancePair>>() {
                    @Override
                    public Iterable<BinancePair> apply(List<BinancePair> binancePairs) throws Exception {
                        return binancePairs;
                    }
                })
                .map(new Function<BinancePair, Pair>() {
                    @Override
                    public Pair apply(BinancePair binancePair) throws Exception {
                        return PairMapperFactory.binancePairMapper.from(binancePair);
                    }
                })
                .flatMap(savePair)
                .toList()
                .flatMapCompletable(new Function<List<Pair>, CompletableSource>() {
                    @Override
                    public CompletableSource apply(List<Pair> pairList) throws Exception {
                        Exchange exchange = new Exchange();
                        exchange.setName(BinanceRepo.BINANCE_EXCHANGE);
                        return save(exchange);
                    }
                });
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

    private Exchange[] getExchangesFromAssets() {
        String exchangeJSON = AndroidAssetsUtils.readFromAssets(appContext.getAssets(), EXCHANGE_FILE_NAME);
        Exchange[] exchangeArray = GsonHelper.gson().fromJson(exchangeJSON, Exchange[].class);
        return exchangeArray;
    }
}

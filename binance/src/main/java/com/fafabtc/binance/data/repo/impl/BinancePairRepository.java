package com.fafabtc.binance.data.repo.impl;

import com.fafabtc.binance.data.local.dao.BinancePairDao;
import com.fafabtc.binance.data.remote.api.BinanceApi;
import com.fafabtc.binance.data.remote.mapper.BinanceExchangeInfoMapper;
import com.fafabtc.binance.data.repo.BinancePairRepo;
import com.fafabtc.binance.model.BinancePair;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jastrelax on 2018/1/13.
 */
@Singleton
public class BinancePairRepository implements BinancePairRepo {

    @Inject
    BinanceApi api;
    @Inject
    BinancePairDao dao;

    @Inject
    public BinancePairRepository() {
    }

    @Override
    public Single<List<BinancePair>> getCachedBinancePairs() {
        return Single.fromCallable(new Callable<List<BinancePair>>() {
            @Override
            public List<BinancePair> call() throws Exception {
                return dao.findAll();
            }
        });
    }

    @Override
    public Single<BinancePair> getBinancePair(final String symbol) {
        return Single.fromCallable(new Callable<BinancePair>() {
            @Override
            public BinancePair call() throws Exception {
                return dao.findBySymbol(symbol);
            }
        });
    }

    @Override
    public Single<List<BinancePair>> getLatestBinancePairs() {
        return api.exchangeInfo()
                .map(BinanceExchangeInfoMapper.MAPPER)
                .flattenAsObservable(new Function<List<BinancePair>, Iterable<BinancePair>>() {
                    @Override
                    public Iterable<BinancePair> apply(List<BinancePair> pairs) throws Exception {
                        return pairs;
                    }
                })
                .filter(new Predicate<BinancePair>() {
                    @Override
                    public boolean test(BinancePair binancePair) throws Exception {
                        return !binancePair.getSymbol().equalsIgnoreCase("123456");
                    }
                })
                .flatMap(new Function<BinancePair, ObservableSource<BinancePair>>() {
                    @Override
                    public ObservableSource<BinancePair> apply(final BinancePair binancePair) throws Exception {
                        return Observable.fromCallable(new Callable<BinancePair>() {
                            @Override
                            public BinancePair call() throws Exception {
                                dao.insert(binancePair);
                                return binancePair;
                            }
                        }).onErrorReturnItem(binancePair);
                    }
                }).toList();

    }
}

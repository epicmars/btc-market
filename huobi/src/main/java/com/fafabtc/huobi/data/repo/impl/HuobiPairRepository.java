package com.fafabtc.huobi.data.repo.impl;

import com.fafabtc.huobi.data.local.dao.HuobiPairDao;
import com.fafabtc.huobi.data.remote.api.HuobiApi;
import com.fafabtc.huobi.data.remote.mapper.HuobiCommonSymbolsMapper;
import com.fafabtc.huobi.data.repo.HuobiPairRepo;
import com.fafabtc.huobi.domain.entity.HuobiPair;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Singleton
public class HuobiPairRepository implements HuobiPairRepo {

    @Inject
    HuobiApi api;

    @Inject
    HuobiPairDao dao;

    @Inject
    public HuobiPairRepository() {
    }

    @Override
    public Single<List<HuobiPair>> getLatestPairs() {
        return api.commonSymbols()
                .map(HuobiCommonSymbolsMapper.MAPPER)
                .flattenAsObservable(new Function<List<HuobiPair>, Iterable<HuobiPair>>() {
                    @Override
                    public Iterable<HuobiPair> apply(List<HuobiPair> pairList) throws Exception {
                        return pairList;
                    }
                })
                .flatMap(new Function<HuobiPair, ObservableSource<HuobiPair>>() {
                    @Override
                    public ObservableSource<HuobiPair> apply(final HuobiPair huobiPair) throws Exception {

                        return Observable.fromCallable(new Callable<HuobiPair>() {
                            @Override
                            public HuobiPair call() throws Exception {
                                dao.insert(huobiPair);
                                return huobiPair;
                            }
                        }).onErrorReturnItem(huobiPair);
                    }
                })
                .toList();
    }

    @Override
    public Single<List<HuobiPair>> getPairsFromDb() {
        return Single.fromCallable(new Callable<List<HuobiPair>>() {
            @Override
            public List<HuobiPair> call() throws Exception {
                return dao.findAll();
            }
        });
    }

    @Override
    public Single<HuobiPair> getPairFromDb(final String symbol) {
        return Single.fromCallable(new Callable<HuobiPair>() {
            @Override
            public HuobiPair call() throws Exception {
                return dao.findBySymbol(symbol);
            }
        });
    }
}

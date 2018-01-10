package com.fafabtc.gateio.data.repo.impl;

import com.fafabtc.common.utils.StringUtils;
import com.fafabtc.gateio.data.local.dao.GateioPairDao;
import com.fafabtc.gateio.data.remote.api.GateioApi;
import com.fafabtc.gateio.data.repo.GateioPairRepo;
import com.fafabtc.gateio.model.entity.GateioPair;

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
 * Created by jastrelax on 2018/1/8.
 */
@Singleton
public class GateioPairRepository implements GateioPairRepo {

    @Inject
    GateioApi api;

    @Inject
    GateioPairDao dao;

    @Inject
    public GateioPairRepository() {
    }

    @Override
    public Single<List<GateioPair>> getGateioPairs() {
        return api.pairs()
                .toObservable()
                .flatMap(new Function<String[], ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String[] strings) throws Exception {
                        return Observable.fromArray(strings);
                    }
                })
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return !StringUtils.isBlank(s);
                    }
                })
                .map(new Function<String, GateioPair>() {
                    @Override
                    public GateioPair apply(String pairName) throws Exception {
                        GateioPair pair = new GateioPair();
                        pair.setName(pairName);
                        String[] baseQuote = pairName.split("_");
                        pair.setBase(baseQuote[0]);
                        pair.setQuote(baseQuote[1]);
                        return pair;
                    }
                })
                .flatMap(new Function<GateioPair, ObservableSource<GateioPair>>() {
                    @Override
                    public ObservableSource<GateioPair> apply(final GateioPair gateioPair) throws Exception {
                        return Observable.fromCallable(new Callable<GateioPair>() {
                            @Override
                            public GateioPair call() throws Exception {
                                dao.insert(gateioPair);
                                return gateioPair;
                            }
                        }).onErrorReturnItem(gateioPair);
                    }
                })
                .toList();
    }
}

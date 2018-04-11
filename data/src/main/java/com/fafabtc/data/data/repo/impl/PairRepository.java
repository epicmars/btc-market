package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.local.dao.PairDao;
import com.fafabtc.data.data.repo.PairRepo;
import com.fafabtc.data.model.entity.exchange.Pair;

import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

/**
 * Created by jastrelax on 2018/4/11.
 */
@Singleton
public class PairRepository implements PairRepo{

    private PairDao pairDao;

    @Inject
    public PairRepository(PairDao pairDao) {
        this.pairDao = pairDao;
    }

    @Override
    public Completable save(final Pair pair) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                pairDao.insertOne(pair);
            }
        });
    }

    @Override
    public Single<Integer> baseCount(final String exchange) {
        return Single.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return pairDao.countBases(exchange);
            }
        });
    }
}

package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.local.dao.PortfolioDao;
import com.fafabtc.data.data.repo.PortfolioRepo;
import com.fafabtc.data.model.entity.exchange.Portfolio;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Singleton
public class PortfolioRepository implements PortfolioRepo {

    @Inject
    PortfolioDao portfolioDao;

    @Inject
    public PortfolioRepository() {
    }

    @Override
    public Completable save(final Portfolio... portfolios) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                portfolioDao.insert(portfolios);
            }
        });
    }

    @Override
    public Completable update(final Portfolio... portfolios) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                portfolioDao.update(portfolios);
            }
        });
    }

    @Override
    public Single<Portfolio> getCurrent() {
        return Single.fromCallable(new Callable<Portfolio>() {
            @Override
            public Portfolio call() throws Exception {
                return portfolioDao.findCurrent();
            }
        });
    }

    @Override
    public Single<Portfolio> getByUUID(final String uuid) {
        return Single.fromCallable(new Callable<Portfolio>() {
            @Override
            public Portfolio call() throws Exception {
                return portfolioDao.findByUUID(uuid);
            }
        });
    }

    @Override
    public Single<Portfolio> getByName(final String name) {
        return Single.fromCallable(new Callable<Portfolio>() {
            @Override
            public Portfolio call() throws Exception {
                return portfolioDao.findByName(name);
            }
        });
    }

    @Override
    public Single<Boolean> isCreated(final String name) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return portfolioDao.findByName(name) != null;
            }
        });
    }

    @Override
    public Single<List<Portfolio>> getAll() {
        return Single.fromCallable(new Callable<List<Portfolio>>() {
            @Override
            public List<Portfolio> call() throws Exception {
                return portfolioDao.findAll();
            }
        });
    }

    @Override
    public Completable delete(final Portfolio portfolio) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                portfolioDao.delete(portfolio);
            }
        });
    }

    @Override
    public Single<Portfolio> create(final String assetsName) {
        return Single.fromCallable(new Callable<Portfolio>() {
            @Override
            public Portfolio call() throws Exception {
                return portfolioDao.create(assetsName);
            }
        });
    }
}

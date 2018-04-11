package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.repo.DataRepo;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.data.repo.TickerRepo;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;

/**
 * Created by jastrelax on 2018/1/9.
 */
@Singleton
public class DataRepository implements DataRepo {

    @Inject
    ExchangeAssetsRepo exchangeAssetsRepo;

    @Inject
    TickerRepo tickerRepo;

    @Inject
    public DataRepository() {
    }

    @Override
    public Completable initiate() {
        return exchangeAssetsRepo.init();
    }

    @Override
    public Completable refreshTickers() {
        return tickerRepo.getAllLatestTickers().toCompletable();
    }

    @Override
    public Completable refreshTickers(String exchange) {
        return tickerRepo.getLatestTickers(exchange, new Date()).toCompletable();
    }
}

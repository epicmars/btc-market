package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.data.repo.DataRepo;
import com.fafabtc.data.data.repo.ExchangeRepo;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.gateio.data.repo.GateioRepo;
import com.fafabtc.gateio.data.repo.GateioTickerRepo;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;

/**
 * Created by jastrelax on 2018/1/9.
 */
@Singleton
public class DataRepository implements DataRepo {

    @Inject
    GateioRepo gateioRepo;

    @Inject
    GateioTickerRepo gateioTickerRepo;

    @Inject
    AccountAssetsRepo accountAssetsRepo;

    @Inject
    ExchangeRepo exchangeRepo;

    @Inject
    TickerRepo tickerRepo;

    @Inject
    public DataRepository() {
    }

    @Override
    public Completable init() {
        return Completable.concatArray(
                exchangeRepo.init(),
                // after all pairs of exchanges has been initialized.
                accountAssetsRepo.init(),
                refreshTickers()
        );
    }

    @Override
    public Completable refreshTickers() {
        return tickerRepo.getAllLatestTickers().toCompletable();
    }
}

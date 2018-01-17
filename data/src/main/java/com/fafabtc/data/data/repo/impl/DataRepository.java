package com.fafabtc.data.data.repo.impl;

import android.content.Context;
import android.content.Intent;

import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.data.repo.DataRepo;
import com.fafabtc.data.data.repo.ExchangeRepo;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.gateio.data.repo.GateioRepo;
import com.fafabtc.gateio.data.repo.GateioTickerRepo;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
    Context context;

    @Inject
    public DataRepository() {
    }

    @Override
    public Completable init() {
        return Completable.concatArray(
                exchangeRepo.init().doOnSubscribe(sendBroadcastActions(DataBroadcasts.Actions.ACTION_INITIATE_EXCHANGE)),
                // after all pairs of exchanges has been initialized.
                accountAssetsRepo.init().doOnSubscribe(sendBroadcastActions(DataBroadcasts.Actions.ACTION_INITIATE_ASSETS)),
                refreshTickers().doOnSubscribe(sendBroadcastActions(DataBroadcasts.Actions.ACTION_FETCH_TICKERS))
        );
    }

    private Consumer<Disposable> sendBroadcastActions(final String action) {
        return new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                context.sendBroadcast(new Intent(action));
            }
        };
    }

    @Override
    public Completable refreshTickers() {
        return tickerRepo.getAllLatestTickers().toCompletable();
    }
}

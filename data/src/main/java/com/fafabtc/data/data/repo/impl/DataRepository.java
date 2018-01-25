package com.fafabtc.data.data.repo.impl;

import android.content.Context;
import android.content.Intent;

import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.data.repo.DataRepo;
import com.fafabtc.data.data.repo.ExchangeRepo;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.data.global.AssetsStateRepository;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jastrelax on 2018/1/9.
 */
@Singleton
public class DataRepository implements DataRepo {

    @Inject
    AccountAssetsRepo accountAssetsRepo;

    @Inject
    ExchangeRepo exchangeRepo;

    @Inject
    TickerRepo tickerRepo;

    @Inject
    Context context;

    @Inject
    AssetsStateRepository assetsStateRepository;

    @Inject
    public DataRepository() {
    }

    @Override
    public Completable init() {
        return Completable.concatArray(
                exchangeRepo.init()
                        .doOnSubscribe(sendBroadcastActions(DataBroadcasts.Actions.ACTION_INITIATE_EXCHANGE)),
                // after all pairs of exchanges has been initialized.
                accountAssetsRepo.init()
                        .doOnSubscribe(sendBroadcastActions(DataBroadcasts.Actions.ACTION_INITIATE_ASSETS)),
                assetsStateRepository.getUpdateTime()
                .filter(new Predicate<Date>() {
                    @Override
                    public boolean test(Date date) throws Exception {
                        long elapsed = System.currentTimeMillis() - date.getTime();
                        return elapsed > 30 * 1000;
                    }
                }).flatMapCompletable(new Function<Date, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Date date) throws Exception {
                        return refreshTickers()
                                .doOnSubscribe(sendBroadcastActions(DataBroadcasts.Actions.ACTION_FETCH_TICKERS));
                    }
                })

        ).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                context.sendBroadcast(new Intent(DataBroadcasts.Actions.ACTION_DATA_INITIALIZED));
            }
        });
    }

    private <T> Consumer<T> sendBroadcastActions(final String action) {
        return new Consumer<T>() {
            @Override
            public void accept(T disposable) throws Exception {
                context.sendBroadcast(new Intent(action));
            }
        };
    }

    @Override
    public Completable refreshTickers() {
        return tickerRepo.getAllLatestTickers()
                .doOnSuccess(sendBroadcastActions(DataBroadcasts.Actions.ACTION_TICKER_UPDATED))
                .toCompletable()
                .concatWith(assetsStateRepository.setUpdateTime(new Date()));
    }
}

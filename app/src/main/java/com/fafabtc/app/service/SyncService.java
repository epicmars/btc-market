package com.fafabtc.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.data.data.global.AssetsStateRepository;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.data.repo.ExchangeRepo;
import com.fafabtc.data.model.entity.exchange.Exchange;

import java.util.Arrays;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class SyncService extends DaggerService {

    @Inject
    ExchangeAssetsRepo exchangeAssetsRepo;

    @Inject
    ExchangeRepo exchangeRepo;

    @Inject
    AssetsStateRepository assetsStateRepository;

    public static void start(Context context) {
        Intent starter = new Intent(context, SyncService.class);
        context.startService(starter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(assetsSyncReceiver, assetsSyncActionsFilter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(assetsSyncReceiver);
    }

    private IntentFilter assetsSyncActionsFilter = new IntentFilter() {
        {
            addAction(Broadcasts.Actions.ACTION_BALANCE_DEPOSITED);
            addAction(Broadcasts.Actions.ACTION_ORDER_DEAL);
            addAction(Broadcasts.Actions.ACTION_ORDER_CREATED);
            addAction(Broadcasts.Actions.ACTION_ASSETS_CREATED);
            addAction(Broadcasts.Actions.ACTION_CURRENT_ASSETS_CHANGED);
        }
    };

    private BroadcastReceiver assetsSyncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Eliminate business coupling of different exchanges.
            exchangeRepo.getExchanges()
                    .flattenAsObservable(new Function<Exchange[], Iterable<Exchange>>() {
                        @Override
                        public Iterable<Exchange> apply(Exchange[] exchanges) throws Exception {
                            return Arrays.asList(exchanges);
                        }
                    })
                    .singleOrError()
                    .flatMapCompletable(new Function<Exchange, CompletableSource>() {
                        @Override
                        public CompletableSource apply(final Exchange exchange) throws Exception {
                            return assetsStateRepository.getAssetsInitialized(exchange.getName())
                                    .flatMapCompletable(new Function<Boolean, CompletableSource>() {
                                        @Override
                                        public CompletableSource apply(Boolean aBoolean) throws Exception {
                                            if (aBoolean) {
                                                return exchangeAssetsRepo.cacheExchangeAssetsToFile(exchange);
                                            }
                                            return Completable.complete();
                                        }
                                    });
                        }
                    })
                    .subscribeOn(Schedulers.from(ExecutorManager.getIO()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e);
                        }
                    });
        }
    };
}

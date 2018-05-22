package com.fafabtc.app.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.global.ExchangeStateRepository;
import com.fafabtc.data.data.repo.DataRepo;
import com.fafabtc.data.data.global.AssetsStateRepository;
import com.fafabtc.data.data.repo.ExchangeRepo;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class MainService extends DaggerService {

    private static final int MSG_REFRESH_TICKER = 1;
    public static final int UPDATE_PERIOD = 60 * 1000;
    private static final String KEY_EXCHANGE = "MainService.KEY_EXCHANGE";

    @Inject
    DataRepo dataRepo;

    @Inject
    AssetsStateRepository assetsStateRepository;

    @Inject
    ExchangeStateRepository exchangeStateRepository;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_TICKER:
                    refreshTickers(msg.getData().getString(KEY_EXCHANGE));
                    return true;
            }
            return false;
        }
    });

    private ScheduledExecutorService scheduledExecutorService;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainService.class);
        context.startService(starter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        startRefreshTickers();
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initiateData();
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopUpdating();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUpdating();
    }

    private void stopUpdating() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
            scheduledExecutorService = null;
        }
    }

    private void initiateData() {
        dataRepo.initiate()
                .compose(RxUtils.completableAsyncIO())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.d("initiateData complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    private void startRefreshTickers() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        for (String exchange : ExchangeRepo.EXCHANGES) {
            startRefreshTickers(exchange);
        }
    }

    private void startRefreshTickers(final String exchange) {
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage(MSG_REFRESH_TICKER);
                message.getData().putString(KEY_EXCHANGE, exchange);
                message.sendToTarget();
            }
        }, getInitialDelay(exchange), UPDATE_PERIOD, TimeUnit.MILLISECONDS);
    }

    private long initialDelay = 0;
    private long getInitialDelay(String exchange) {
        initialDelay = 0;
        exchangeStateRepository.getUpdateTime(exchange)
                .subscribe(new SingleObserver<Date>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Date date) {
                        long updatetime = date.getTime();
                        long now = new Date().getTime();
                        long elapsed = now - updatetime;
                        initialDelay = elapsed > UPDATE_PERIOD ? 0 : UPDATE_PERIOD - elapsed;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
        return initialDelay;
    }

    private void refreshTickers(String exchange) {
        dataRepo.refreshTickers(exchange)
                .subscribeOn(Schedulers.from(ExecutorManager.getIO()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("refreshTickers complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
}

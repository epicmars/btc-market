package com.fafabtc.app.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fafabtc.app.constants.Broadcast;
import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.DataRepo;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class MainService extends DaggerService {

    @Inject
    DataRepo dataRepo;

    private Observable<Long> refreshTicker;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainService.class);
        context.startService(starter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initData();
        startRefreshTickers();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        dataRepo.init()
                .compose(RxUtils.completableAsyncIO())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        sendBroadcast(new Intent(Broadcast.Actions.ACTION_DATA_INITIALIZED));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    private void startRefreshTickers() {
        if (refreshTicker == null) {
            // todo use count down or other method to tick
            refreshTicker = Observable.interval(5, 60, TimeUnit.SECONDS);
            refreshTicker
                    .observeOn(Schedulers.from(ExecutorManager.getIO()))
                    .flatMapCompletable(new Function<Long, CompletableSource>() {
                        @Override
                        public CompletableSource apply(Long aLong) throws Exception {
                            return dataRepo.refreshTickers()
                                    .doOnComplete(new Action() {
                                        @Override
                                        public void run() throws Exception {
                                            sendBroadcast(new Intent(Broadcast.Actions.ACTION_TICKER_UPDATED));
                                        }
                                    });
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Timber.d("");
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
    }
}

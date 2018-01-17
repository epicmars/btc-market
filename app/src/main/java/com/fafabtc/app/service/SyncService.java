package com.fafabtc.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.common.file.FileUtils;
import com.fafabtc.common.json.GsonHelper;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.global.AssetsStateRepository;
import com.fafabtc.data.model.vo.AccountAssetsData;

import java.util.List;

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

import static com.fafabtc.data.global.AssetsState.ASSETS_DATA_FILE;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class SyncService extends DaggerService {

    @Inject
    ExchangeAssetsRepo exchangeAssetsRepo;

    @Inject
    AccountAssetsRepo accountAssetsRepo;

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
            assetsStateRepository.isAssetsInitialized()
                    .flatMapCompletable(new Function<Boolean, CompletableSource>() {
                        @Override
                        public CompletableSource apply(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                return exchangeAssetsRepo.getAllAccountAssetsData()
                                        .flatMapCompletable(new Function<List<AccountAssetsData>, CompletableSource>() {
                                            @Override
                                            public CompletableSource apply(List<AccountAssetsData> exchangeAssets) throws Exception {
                                                if (!exchangeAssets.isEmpty()) {
                                                    FileUtils.writeFile(ASSETS_DATA_FILE, GsonHelper.prettyGson().toJson(exchangeAssets));
                                                }
                                                return Completable.complete();
                                            }
                                        });
                            }
                            return Completable.complete();
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

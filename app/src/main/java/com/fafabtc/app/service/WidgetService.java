package com.fafabtc.app.service;

import android.content.Intent;
import android.os.IBinder;

import com.fafabtc.app.constants.Services;
import com.fafabtc.app.receiver.AssetsWidgetProvider;
import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.app.utils.TickersAlarmUtils;
import com.fafabtc.app.utils.WidgetUtils;
import com.fafabtc.data.data.repo.PortfolioRepo;
import com.fafabtc.data.data.repo.AssetsStatisticsRepo;
import com.fafabtc.data.data.repo.DataRepo;
import com.fafabtc.data.data.global.AssetsStateRepository;
import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.data.model.vo.WidgetData;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class WidgetService extends DaggerService {

    @Inject
    DataRepo dataRepo;

    @Inject
    PortfolioRepo portfolioRepo;

    @Inject
    AssetsStatisticsRepo assetsStatisticsRepo;

    @Inject
    AssetsStateRepository assetsStateRepository;

    public static final int MIN_UPDATE_INTERVAL = 60 * 1000;

    private boolean isUpdating = false;

    public WidgetService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (WidgetUtils.isWidgetAvailable(this)) {
            TickersAlarmUtils.cancelUpdate(this);
            TickersAlarmUtils.scheduleUpdate(this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Services.Actions.ACTION_UPDATE_TICKERS:
                    updateTickers();
                    break;
                case Services.Actions.ACTION_MANUAL_UPDATE_TICKERS:
                    AssetsWidgetProvider.showLoadingProgress(this);
                    updateTickers();
                    break;
                case Services.Actions.ACTION_UPDATE_WIDGET:
                    updateWidgetData();
                    break;
            }
        }
        return START_STICKY;
    }

    private void updateTickers() {
        if (isUpdating) return;
        isUpdating = true;
        dataRepo.refreshTickers()
                .subscribeOn(Schedulers.from(ExecutorManager.getIO()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        isUpdating = false;
                        Timber.d("onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        isUpdating = false;
                        Timber.e(e);
                        AssetsWidgetProvider.hideLoadingProgress(WidgetService.this);
                    }
                });
    }

    private void updateWidgetData() {
        final WidgetData widgetData = new WidgetData();
        portfolioRepo.getCurrent()
                .flatMap(new Function<Portfolio, SingleSource<Double>>() {
                    @Override
                    public SingleSource<Double> apply(Portfolio portfolio) throws Exception {
                        widgetData.setPortfolio(portfolio);
                        return assetsStatisticsRepo.getAccountTotalVolume(portfolio.getUuid());
                    }
                })
                .map(new Function<Double, WidgetData>() {
                    @Override
                    public WidgetData apply(Double aDouble) throws Exception {
                        widgetData.setVolume(aDouble);
                        return widgetData;
                    }
                })
                .subscribeOn(Schedulers.from(ExecutorManager.getNOW()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<WidgetData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(WidgetData data) {
                        AssetsWidgetProvider.update(WidgetService.this, data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
}

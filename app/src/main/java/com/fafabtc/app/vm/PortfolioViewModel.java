package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.PortfolioRepo;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.model.entity.exchange.Portfolio;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class PortfolioViewModel extends ViewModel{

    @Inject
    PortfolioRepo portfolioRepo;

    @Inject
    ExchangeAssetsRepo exchangeAssetsRepo;

    private MutableLiveData<Portfolio> currentPortfolio;

    private MutableLiveData<Boolean> isCurrentPortfolioChanged;

    private MutableLiveData<Boolean> isAssetsInitialized;

    @Inject
    public PortfolioViewModel() {
        currentPortfolio = new MutableLiveData<>();
        isCurrentPortfolioChanged = new MutableLiveData<>();
        isAssetsInitialized = new MutableLiveData<>();
    }

    public void loadAccountList() {
        exchangeAssetsRepo.hasExchangeAssetsInitialized()
                .compose(RxUtils.<Boolean>singleAsyncIO())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        isAssetsInitialized.setValue(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public void loadCurrentAccount() {
        portfolioRepo.getCurrent()
                .compose(RxUtils.<Portfolio>singleAsyncIO())
                .subscribe(new SingleObserver<Portfolio>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Portfolio portfolio) {
                        currentPortfolio.setValue(portfolio);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public void currentAccountChanged() {
        isCurrentPortfolioChanged.setValue(true);
    }

    public MutableLiveData<Boolean> isAssetsInitialized() {
        return isAssetsInitialized;
    }

    public MutableLiveData<Boolean> isCurrentPortfolioChanged() {
        return isCurrentPortfolioChanged;
    }

    public MutableLiveData<Portfolio> getCurrentPortfolio() {
        return currentPortfolio;
    }

}

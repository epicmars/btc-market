package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.app.vm.exception.ViewModelException;
import com.fafabtc.data.data.global.AssetsStateRepository;
import com.fafabtc.data.data.repo.PortfolioRepo;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.domain.model.Resource;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class PortfolioCreateViewModel extends ViewModel {

    @Inject
    PortfolioRepo portfolioRepo;

    @Inject
    ExchangeAssetsRepo exchangeAssetsRepo;

    MutableLiveData<Resource<Portfolio>> portfolioMutableLiveData = new MutableLiveData<>();

    @Inject
    public PortfolioCreateViewModel() {
    }

    /**
     * 如果已经创建过该名称的资产
     *
     * @param assetsName
     */
    public void createPortfolio(final String assetsName) {
        final Single<Portfolio> created = exchangeAssetsRepo.hasExchangeInitialized()
                .flatMap(new Function<Boolean, SingleSource<Portfolio>>() {
                    @Override
                    public SingleSource<Portfolio> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean)
                            return exchangeAssetsRepo.createPortfolioOfExchange(assetsName);
                        throw new ViewModelException("资产初始化中");
                    }
                });

        portfolioRepo.isCreated(assetsName)
                .flatMap(new Function<Boolean, SingleSource<Portfolio>>() {
                    @Override
                    public SingleSource<Portfolio> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean)
                            throw new ViewModelException(String.format("已存在\"%s\"资产组合", assetsName));
                        return created;
                    }
                })
                .compose(RxUtils.<Portfolio>singleAsyncIO())
                .subscribe(new SingleObserver<Portfolio>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Portfolio portfolio) {
                        portfolioMutableLiveData.setValue(Resource.success(portfolio));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        String message = e.getMessage();
                        portfolioMutableLiveData.setValue(Resource.error(message, new Portfolio()));
                    }
                });
    }

    public MutableLiveData<Resource<Portfolio>> getPortfolio() {
        return portfolioMutableLiveData;
    }
}

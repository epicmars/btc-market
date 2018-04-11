package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/22.
 */

public class MainViewModel extends ViewModel{

    private MutableLiveData<Boolean> isDataInitialized = new MutableLiveData<>();

    @Inject
    ExchangeAssetsRepo exchangeAssetsRepo;

    @Inject
    public MainViewModel() {
    }

    public void queryIsDataInitialized() {
        exchangeAssetsRepo.isExchangeAssetsInitialized()
                .compose(RxUtils.<Boolean>singleAsyncIO())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        isDataInitialized.setValue(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public MutableLiveData<Boolean> isDataInitialized() {
        return isDataInitialized;
    }
}

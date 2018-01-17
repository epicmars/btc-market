package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.data.global.AssetsStateRepository;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by jastrelax on 2018/1/22.
 */

public class MainViewModel extends ViewModel{

    private MutableLiveData<Boolean> isDataInitialized = new MutableLiveData<>();

    @Inject
    AssetsStateRepository stateRepository;

    @Inject
    public MainViewModel() {
    }

    public void queryIsDataInitialized() {
        stateRepository.isAssetsInitialized()
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

                    }
                });
    }

    public MutableLiveData<Boolean> isDataInitialized() {
        return isDataInitialized;
    }
}

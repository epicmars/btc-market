package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.global.AssetsStateRepository;
import com.fafabtc.data.model.entity.exchange.AccountAssets;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class AccountViewModel extends ViewModel{

    @Inject
    AccountAssetsRepo accountAssetsRepo;

    @Inject
    AssetsStateRepository assetsStateRepository;

    private MutableLiveData<AccountAssets> currentAccountAssets;

    private MutableLiveData<Boolean> isCurrentAccountChanged;

    private MutableLiveData<Boolean> isAssetsInitialized;

    @Inject
    public AccountViewModel() {
        currentAccountAssets = new MutableLiveData<>();
        isCurrentAccountChanged = new MutableLiveData<>();
        isAssetsInitialized = new MutableLiveData<>();
    }

    public void loadAccountList() {
        assetsStateRepository.isAssetsInitialized()
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
        accountAssetsRepo.getCurrent()
                .compose(RxUtils.<AccountAssets>singleAsyncIO())
                .subscribe(new SingleObserver<AccountAssets>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(AccountAssets accountAssets) {
                        currentAccountAssets.setValue(accountAssets);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public void currentAccountChanged() {
        isCurrentAccountChanged.setValue(true);
    }

    public MutableLiveData<Boolean> isAssetsInitialized() {
        return isAssetsInitialized;
    }

    public MutableLiveData<Boolean> isCurrentAccountChanged() {
        return isCurrentAccountChanged;
    }

    public MutableLiveData<AccountAssets> getCurrentAccountAssets() {
        return currentAccountAssets;
    }

}

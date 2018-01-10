package com.fafabtc.app.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class AccountAssetsCreateViewModel extends ViewModel{

    @Inject
    AccountAssetsRepo accountAssetsRepo;

    MutableLiveData<AccountAssets> assetsMutableLiveData = new MutableLiveData<>();

    @Inject
    public AccountAssetsCreateViewModel() {
    }

    public void createAccountAssets(String assetsName) {
        accountAssetsRepo.createAssets(assetsName)
                .compose(RxUtils.<AccountAssets>singleAsyncIO())
                .subscribe(new SingleObserver<AccountAssets>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(AccountAssets accountAssets) {
                        assetsMutableLiveData.setValue(accountAssets);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public LiveData<AccountAssets> getAccountAssets() {
        return assetsMutableLiveData;
    }
}

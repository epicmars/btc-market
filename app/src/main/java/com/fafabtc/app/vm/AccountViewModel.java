package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;

import java.util.List;

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

    private MutableLiveData<AccountAssets> currentAccountAssets;

    private MutableLiveData<Boolean> currentAccountChanged;

    private MutableLiveData<List<AccountAssets>> accountAssetsList;

    @Inject
    public AccountViewModel() {
        accountAssetsList = new MutableLiveData<>();
        currentAccountAssets = new MutableLiveData<>();
        currentAccountChanged = new MutableLiveData<>();
    }

    public void updateAccountList() {
        accountAssetsRepo.getAllAssets()
                .compose(RxUtils.<List<AccountAssets>>singleAsyncIO())
                .subscribe(new SingleObserver<List<AccountAssets>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<AccountAssets> accountAssets) {
                        accountAssetsList.setValue(accountAssets);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public void currentAccountChanged() {
        currentAccountChanged.setValue(true);
    }

    public MutableLiveData<Boolean> getCurrentAccountChanged() {
        return currentAccountChanged;
    }

    public MutableLiveData<AccountAssets> getCurrentAccountAssets() {
        return currentAccountAssets;
    }

    public MutableLiveData<List<AccountAssets>> getAccountAssetsList() {
        return accountAssetsList;
    }
}

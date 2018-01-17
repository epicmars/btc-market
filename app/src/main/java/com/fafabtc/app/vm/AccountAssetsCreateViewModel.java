package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.sqlite.SQLiteException;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.app.vm.exception.ViewModelException;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.global.AssetsStateRepository;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.domain.model.Resource;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class AccountAssetsCreateViewModel extends ViewModel{

    @Inject
    AccountAssetsRepo accountAssetsRepo;

    @Inject
    AssetsStateRepository assetsStateRepository;

    MutableLiveData<Resource<AccountAssets>> assetsMutableLiveData = new MutableLiveData<>();

    @Inject
    public AccountAssetsCreateViewModel() {
    }

    public void createAccountAssets(final String assetsName) {
        assetsStateRepository.isAssetsInitialized()
                .flatMap(new Function<Boolean, SingleSource<AccountAssets>>() {
                    @Override
                    public SingleSource<AccountAssets> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return accountAssetsRepo.createAssets(assetsName);
                        }
                        throw new ViewModelException("资产初始化中");
                    }
                })
                .compose(RxUtils.<AccountAssets>singleAsyncIO())
                .subscribe(new SingleObserver<AccountAssets>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(AccountAssets accountAssets) {
                        assetsMutableLiveData.setValue(Resource.success(accountAssets));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        String message = e.getMessage();
                        if (e instanceof SQLiteException) {
                            message = String.format("已存在\"%s\"资产组合", assetsName);
                        }
                        assetsMutableLiveData.setValue(Resource.error(message, new AccountAssets()));
                    }
                });
    }

    public MutableLiveData<Resource<AccountAssets>> getAccountAssets() {
        return assetsMutableLiveData;
    }
}

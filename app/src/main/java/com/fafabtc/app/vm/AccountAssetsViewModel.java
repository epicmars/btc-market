package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.app.vm.exception.ViewModelException;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.global.AssetsStateRepository;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.domain.model.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/17.
 */

public class AccountAssetsViewModel extends ViewModel {

    @Inject
    AccountAssetsRepo accountAssetsRepo;

    @Inject
    AssetsStateRepository assetsStateRepository;

    private MutableLiveData<Resource<List<AccountAssets>>> accountAssetsList;


    @Inject
    public AccountAssetsViewModel() {
        accountAssetsList = new MutableLiveData<>();
    }


    public void loadAccountAssetsList() {
        assetsStateRepository.isAssetsInitialized()
                .flatMap(new Function<Boolean, SingleSource<List<AccountAssets>>>() {
                    @Override
                    public SingleSource<List<AccountAssets>> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return accountAssetsRepo.getAllAssets();
                        } else {
                            throw new ViewModelException("资产初始化中");
                        }
                    }
                })
                .compose(RxUtils.<List<AccountAssets>>singleAsyncIO())
                .subscribe(new SingleObserver<List<AccountAssets>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<AccountAssets> accountAssets) {
                        accountAssetsList.setValue(Resource.success(accountAssets));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        List<AccountAssets> assetsList = new ArrayList<>();
                        accountAssetsList.setValue(Resource.error(e.getMessage(), assetsList));
                    }
                });
    }

    public MutableLiveData<Resource<List<AccountAssets>>> getAccountAssetsList() {
        return accountAssetsList;
    }


}

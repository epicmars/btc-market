package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.base.model.Resource;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/9.
 */

public class BalanceDepositViewModel extends ViewModel {

    private BlockchainAssets blockchainAssets;

    @Inject
    BlockchainAssetsRepo blockchainAssetsRepo;

    MutableLiveData<Resource<Boolean>> isDepositSucceed = new MutableLiveData<>();

    @Inject
    public BalanceDepositViewModel() {
    }

    public void depositBalance(double balance) {
        blockchainAssets.setPrinciple(blockchainAssets.getPrinciple() + balance);
        blockchainAssets.setAvailable(blockchainAssets.getAvailable() + balance);
        blockchainAssetsRepo.update(blockchainAssets)
                .compose(RxUtils.completableAsyncIO())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        isDepositSucceed.setValue(Resource.success(true));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        isDepositSucceed.setValue(Resource.error(null, false));
                    }
                });
    }

    public MutableLiveData<Resource<Boolean>> getIsDepositSucceed() {
        return isDepositSucceed;
    }

    public void setBlockchainAssets(BlockchainAssets blockchainAssets) {
        this.blockchainAssets = blockchainAssets;
    }
}

package com.fafabtc.app.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.vo.ExchangeAssets;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class ExchangeAssetsViewModel extends ViewModel{

    @Inject
    ExchangeAssetsRepo exchangeAssetsRepo;

    private AccountAssets accountAssets;
    private Exchange exchange;

    private MutableLiveData<ExchangeAssets> exchangeAssets = new MutableLiveData<>();

    @Inject
    public ExchangeAssetsViewModel() {
    }

    public void updateExchangeAssets() {
        exchangeAssetsRepo.getExchangeAssets(accountAssets, exchange)
                .compose(RxUtils.<ExchangeAssets>singleAsyncIO())
                .subscribe(new SingleObserver<ExchangeAssets>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ExchangeAssets assets) {
                        exchangeAssets.setValue(assets);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public LiveData<ExchangeAssets> getExchangeAssets() {
        return exchangeAssets;
    }

    public void setAccountAssets(AccountAssets accountAssets) {
        this.accountAssets = accountAssets;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
}

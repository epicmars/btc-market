package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.data.repo.OrderRepo;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Order;
import com.fafabtc.data.model.entity.exchange.Ticker;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/13.
 */

public class TradeSellViewModel extends ViewModel {

    @Inject
    BlockchainAssetsRepo blockchainAssetsRepo;

    @Inject
    OrderRepo orderRepo;

    private MutableLiveData<BlockchainAssets> baseBlockchainAssets = new MutableLiveData<>();

    private Ticker ticker;

    @Inject
    public TradeSellViewModel() {
    }

    public void updateBaseBalance() {
        blockchainAssetsRepo.getFromCurrentPortfolio(ticker.getExchange(), ticker.getBase())
                .compose(RxUtils.<BlockchainAssets>singleAsyncIO())
                .subscribe(new SingleObserver<BlockchainAssets>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BlockchainAssets blockchainAssets) {
                        baseBlockchainAssets.setValue(blockchainAssets);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public void sellBlockchainAssets(final double price, final double quantity) {
        blockchainAssetsRepo.getFromCurrentPortfolio(ticker.getExchange(), ticker.getBase())
                .flatMapCompletable(new Function<BlockchainAssets, CompletableSource>() {
                    @Override
                    public CompletableSource apply(BlockchainAssets baseAssets) throws Exception {
                        return orderRepo.createNewOrder(baseAssets.getAssetsUUID(),
                                baseAssets.getExchange(),
                                price,
                                quantity,
                                ticker.getPair(),
                                ticker.getBase(),
                                ticker.getQuote(),
                                Order.Type.SELL);
                    }
                })
                .compose(RxUtils.completableAsyncIO())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });

    }

    public MutableLiveData<BlockchainAssets> getBaseBlockchainAssets() {
        return baseBlockchainAssets;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }
}

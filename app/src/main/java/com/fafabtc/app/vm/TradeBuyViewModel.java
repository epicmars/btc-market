package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.data.repo.OrderRepo;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Order;
import com.fafabtc.data.model.entity.exchange.Ticker;
import com.fafabtc.domain.model.Resource;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/9.
 */

public class TradeBuyViewModel extends ViewModel {

    @Inject
    BlockchainAssetsRepo blockchainAssetsRepo;

    @Inject
    OrderRepo orderRepo;

    private MutableLiveData<BlockchainAssets> quoteBlockchainAssets = new MutableLiveData<>();

    private MutableLiveData<Resource<Boolean>> isOrderCreated = new MutableLiveData<>();

    private Ticker ticker;

    @Inject
    public TradeBuyViewModel() {

    }

    public void updateQuoteBalance() {
        blockchainAssetsRepo.getFromCurrentPortfolio(ticker.getExchange(), ticker.getQuote())
                .compose(RxUtils.<BlockchainAssets>singleAsyncIO())
                .subscribe(new SingleObserver<BlockchainAssets>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BlockchainAssets b) {
                        quoteBlockchainAssets.setValue(b);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void buyBlockchainAssets(final double price, final double quantity) {
        blockchainAssetsRepo.getFromCurrentPortfolio(ticker.getExchange(), ticker.getQuote())
                .flatMapCompletable(new Function<BlockchainAssets, CompletableSource>() {
                    @Override
                    public CompletableSource apply(BlockchainAssets quoteAssets) throws Exception {
                        return orderRepo.createNewOrder(quoteAssets.getAssetsUUID(),
                                quoteAssets.getExchange(),
                                price,
                                quantity,
                                ticker.getPair(),
                                ticker.getBase(),
                                ticker.getQuote(),
                                Order.Type.BUY);
                    }
                })
                .compose(RxUtils.completableAsyncIO())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        isOrderCreated.setValue(Resource.success(true));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        isOrderCreated.setValue(Resource.error(null, false));
                    }
                });

    }

    public MutableLiveData<Resource<Boolean>> getIsOrderCreated() {
        return isOrderCreated;
    }

    public MutableLiveData<BlockchainAssets> getBalanceAssets() {
        return quoteBlockchainAssets;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }
}

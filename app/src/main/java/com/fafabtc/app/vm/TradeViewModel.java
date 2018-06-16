package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.data.repo.OrderRepo;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Order;
import com.fafabtc.data.model.entity.exchange.Ticker;
import com.fafabtc.base.model.Resource;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Refresh last ticker of current exchange.
 * Created by jastrelax on 2018/1/9.
 */

public class TradeViewModel extends ViewModel {

    @Inject
    TickerRepo tickerRepo;

    @Inject
    BlockchainAssetsRepo blockchainAssetsRepo;

    @Inject
    OrderRepo orderRepo;

    private Ticker ticker;

    private MutableLiveData<Resource<Ticker>> tickerLiveData = new MutableLiveData<>();

    private MutableLiveData<Resource<Boolean>> isOrderCreated = new MutableLiveData<>();

    private MutableLiveData<Resource<BlockchainAssets>> baseBlockchainAssets = new MutableLiveData<>();

    private MutableLiveData<Resource<BlockchainAssets>> quoteBlockchainAssets = new MutableLiveData<>();

    @Inject
    public TradeViewModel() {

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
                        baseBlockchainAssets.setValue(Resource.success(blockchainAssets));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        baseBlockchainAssets.setValue(Resource.error());
                    }
                });
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
                        quoteBlockchainAssets.setValue(Resource.success(b));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        quoteBlockchainAssets.setValue(Resource.error());
                    }
                });
    }

    public void refreshTicker() {
        tickerRepo.getLatestTickerFromCache(ticker.getExchange(), ticker.getBase(), ticker.getQuote())
                .subscribeOn(Schedulers.from(ExecutorManager.getIO()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Ticker>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Ticker ticker) {
                        tickerLiveData.setValue(Resource.success(ticker));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        tickerLiveData.setValue(Resource.error());
                    }
                });
    }


    public void buyBlockchainAssets(final double price, final double quantity) {
        blockchainAssetsRepo.getFromCurrentPortfolio(ticker.getExchange(), ticker.getQuote())
                .flatMapCompletable(new Function<BlockchainAssets, CompletableSource>() {
                    @Override
                    public CompletableSource apply(BlockchainAssets quoteAssets) throws Exception {
                        return orderRepo.createNewOrder(quoteAssets.getAssetsUuid(),
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


    public void sellBlockchainAssets(final double price, final double quantity) {
        blockchainAssetsRepo.getFromCurrentPortfolio(ticker.getExchange(), ticker.getBase())
                .flatMapCompletable(new Function<BlockchainAssets, CompletableSource>() {
                    @Override
                    public CompletableSource apply(BlockchainAssets baseAssets) throws Exception {
                        return orderRepo.createNewOrder(baseAssets.getAssetsUuid(),
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
                        isOrderCreated.setValue(Resource.success(true));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        isOrderCreated.setValue(Resource.error(null, false));
                    }
                });
    }


    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
        this.tickerLiveData.setValue(Resource.loading(ticker));
    }

    public MutableLiveData<Resource<Ticker>> getTickerLiveData() {
        return tickerLiveData;
    }

    public MutableLiveData<Resource<Boolean>> getIsOrderCreated() {
        return isOrderCreated;
    }

    public MutableLiveData<Resource<BlockchainAssets>> getBalanceAssets() {
        return quoteBlockchainAssets;
    }

    public MutableLiveData<Resource<BlockchainAssets>> getBaseBlockchainAssets() {
        return baseBlockchainAssets;
    }
}

package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.data.model.entity.exchange.Ticker;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Refresh last ticker of current exchange.
 * Created by jastrelax on 2018/1/9.
 */

public class TradeViewModel extends ViewModel {

    private Ticker ticker;

    private MutableLiveData<Ticker> tickerLiveData = new MutableLiveData<>();

    @Inject
    TickerRepo tickerRepo;

    @Inject
    public TradeViewModel() {

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
                        tickerLiveData.setValue(ticker);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
        this.tickerLiveData.setValue(ticker);
    }

    public MutableLiveData<Ticker> getTickerLiveData() {
        return tickerLiveData;
    }
}

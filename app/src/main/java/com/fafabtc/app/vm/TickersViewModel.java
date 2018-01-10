package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.data.model.entity.exchange.Ticker;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/10.
 */

public class TickersViewModel extends ViewModel{

    @Inject
    TickerRepo tickerRepo;

    private String exchange;

    private MutableLiveData<List<Ticker>> tickers = new MutableLiveData<>();

    @Inject
    public TickersViewModel() {
    }

    public void updateTickers() {
        tickerRepo.getLatestTickerListFromCache(exchange)
                .subscribeOn(Schedulers.from(ExecutorManager.getIO()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tickerObserver);

    }

    public void getLatestTickers() {
        tickerRepo.getLatestTickers(exchange, new Date())
                .subscribeOn(Schedulers.from(ExecutorManager.getIO()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tickerObserver);
    }

    public MutableLiveData<List<Ticker>> getTickers() {
        return tickers;
    }

    private SingleObserver<List<Ticker>> tickerObserver = new SingleObserver<List<Ticker>>() {

        @Override
        public void onSubscribe(Disposable d) {
            Timber.d("onSubscribe");
        }

        @Override
        public void onSuccess(List<Ticker> tickerList) {
            Collections.sort(tickerList, new Comparator<Ticker>() {
                @Override
                public int compare(Ticker o1, Ticker o2) {
                    return o1.getPair().compareTo(o2.getPair());
                }
            });
            tickers.setValue(tickerList);
        }


        @Override
        public void onError(Throwable t) {
            Timber.e(t);
        }
    };

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}

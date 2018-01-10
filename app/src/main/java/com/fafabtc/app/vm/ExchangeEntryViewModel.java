package com.fafabtc.app.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.data.data.repo.ExchangeRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.vo.ExchangeEntry;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class ExchangeEntryViewModel extends ViewModel {

    @Inject
    ExchangeRepo exchangeRepo;

    private AccountAssets accountAssets;

    MutableLiveData<List<ExchangeEntry>> exchangeList = new MutableLiveData<>();

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ExchangeEntryViewModel() {
    }

    public void loadExchanges() {
        Disposable disposable = exchangeRepo.getExchanges()
                .toObservable()
                .flatMap(new Function<Exchange[], ObservableSource<Exchange>>() {
                    @Override
                    public ObservableSource<Exchange> apply(Exchange[] exchanges) throws Exception {
                        return Observable.fromArray(exchanges);
                    }
                })
                .map(new Function<Exchange, ExchangeEntry>() {
                    @Override
                    public ExchangeEntry apply(Exchange exchange) throws Exception {
                        ExchangeEntry entry = new ExchangeEntry();
                        entry.setExchange(exchange);
                        entry.setAccountAssets(accountAssets);
                        return entry;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.from(ExecutorManager.getNOW()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<ExchangeEntry>>() {
                    @Override
                    public void onSuccess(List<ExchangeEntry> exchangeEntries) {
                        exchangeList.setValue(exchangeEntries);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public LiveData<List<ExchangeEntry>> getExchanges() {
        return exchangeList;
    }

    public void setAccountAssets(AccountAssets accountAssets) {
        this.accountAssets = accountAssets;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}

package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.local.dao.AccountAssetsDao;
import com.fafabtc.data.data.local.dao.BalanceAssetsDao;
import com.fafabtc.data.data.repo.BalanceAssetsRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.BalanceAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Deprecated
@Singleton
public class BalanceAssetsRepository implements BalanceAssetsRepo {

    @Inject
    BalanceAssetsDao balanceAssetsDao;

    @Inject
    AccountAssetsDao accountAssetsDao;

    @Inject
    public BalanceAssetsRepository() {
    }

    @Override
    public Completable save(final BalanceAssets balanceAssets) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (balanceAssets.id == null) {
                    balanceAssetsDao.insertOne(balanceAssets);
                } else {
                    balanceAssetsDao.updateOne(balanceAssets);
                }
            }
        });
    }

    @Override
    public Completable update(final BalanceAssets balanceAssets) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                balanceAssetsDao.updateOne(balanceAssets);
            }
        });
    }

    @Override
    public Single<BalanceAssets> getCurrentBalanceAssets(final String exchange, final String name) {
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                return accountAssetsDao.findCurrent();
            }
        }).flatMap(new Function<AccountAssets, SingleSource<? extends BalanceAssets>>() {
            @Override
            public SingleSource<? extends BalanceAssets> apply(AccountAssets accountAssets) throws Exception {
                return getBalanceAssets(accountAssets.getUuid(), exchange, name);
            }
        });
    }

    @Override
    public Single<BalanceAssets> getBalanceAssets(final String assetsUUID, final String exchange, final String name) {
        return Single.fromCallable(new Callable<BalanceAssets>() {
            @Override
            public BalanceAssets call() throws Exception {
                return balanceAssetsDao.find(assetsUUID, exchange, name);
            }
        });
    }

    @Override
    public Single<List<BalanceAssets>> getAccountBalanceAssets(final String assetsUUID) {
        return Single.fromCallable(new Callable<List<BalanceAssets>>() {
            @Override
            public List<BalanceAssets> call() throws Exception {
                return balanceAssetsDao.findByAccount(assetsUUID);
            }
        });
    }

    @Override
    public Completable initExchangeBalanceAssets(final String assetsUUID, final Exchange exchange) {
        String[] currencyNames = {};
        return Observable.fromArray(currencyNames)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final String currency) throws Exception {
                        return Observable.fromCallable(new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                BalanceAssets b = new BalanceAssets();
                                b.setAssetsUUID(assetsUUID);
                                b.setCurrency(currency);
                                b.setExchange(exchange.getName());
                                balanceAssetsDao.insertOne(b);
                                return currency;
                            }
                        }).onErrorReturnItem(currency);
                    }
                })
                .flatMapCompletable(new Function<String, CompletableSource>() {
                    @Override
                    public CompletableSource apply(String s) throws Exception {
                        return Completable.complete();
                    }
                });
    }

    @Override
    public Single<List<BalanceAssets>> getExchangeBalanceAssets(final String assetsUUID, final String exchange) {
        return Single.fromCallable(new Callable<List<BalanceAssets>>() {
            @Override
            public List<BalanceAssets> call() throws Exception {
                return balanceAssetsDao.findByAccountAndExchange(assetsUUID, exchange);
            }
        });
    }
}

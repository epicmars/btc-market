package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.local.dao.AccountAssetsDao;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Singleton
public class AccountAssetsRepository implements AccountAssetsRepo {

    @Inject
    AccountAssetsDao accountAssetsDao;

    @Inject
    public AccountAssetsRepository() {
    }

    @Override
    public Completable save(final AccountAssets... accountAssets) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                accountAssetsDao.insert(accountAssets);
            }
        });
    }

    @Override
    public Completable update(final AccountAssets... accountAssets) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                accountAssetsDao.update(accountAssets);
            }
        });
    }

    @Override
    public Single<AccountAssets> getCurrent() {
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                return accountAssetsDao.findCurrent();
            }
        });
    }

    @Override
    public Single<AccountAssets> getByUUID(final String uuid) {
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                return accountAssetsDao.findByUUID(uuid);
            }
        });
    }

    @Override
    public Single<AccountAssets> getByName(final String name) {
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                return accountAssetsDao.findByName(name);
            }
        });
    }

    @Override
    public Single<Boolean> isCreated(final String name) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return accountAssetsDao.findByName(name) != null;
            }
        });
    }

    @Override
    public Single<List<AccountAssets>> getAll() {
        return Single.fromCallable(new Callable<List<AccountAssets>>() {
            @Override
            public List<AccountAssets> call() throws Exception {
                return accountAssetsDao.findAll();
            }
        });
    }

    @Override
    public Completable delete(final AccountAssets accountAssets) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                accountAssetsDao.delete(accountAssets);
            }
        });
    }

    @Override
    public Single<AccountAssets> create(final String assetsName) {
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                return accountAssetsDao.create(assetsName);
            }
        });
    }
}

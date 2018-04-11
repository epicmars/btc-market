package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.AccountAssets;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface AccountAssetsRepo {

    Completable save(AccountAssets... accountAssets);

    Completable delete(AccountAssets accountAssets);

    Completable update(AccountAssets... accountAssets);

    Single<AccountAssets> getCurrent();

    Single<AccountAssets> getByUUID(String uuid);

    Single<AccountAssets> getByName(String name);

    Single<Boolean> isCreated(String name);

    Single<List<AccountAssets>> getAll();

    Single<AccountAssets> create(String assetsName);
}

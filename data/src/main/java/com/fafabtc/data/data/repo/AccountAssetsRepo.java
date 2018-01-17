package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.vo.AccountAssetsData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface AccountAssetsRepo {

    Completable initAllAccountAssets();

    Completable initAssets(AccountAssets accountAssets);

    Single<AccountAssets> createAssets(String assetsName);

    Completable update(AccountAssets... accountAssets);

    Completable init();

    Completable restore(AccountAssetsData accountAssetsData);

    Single<AccountAssets> getCurrent();

    Single<AccountAssets> getByUUID(String uuid);

    Single<List<AccountAssets>> getAllAssets();
}

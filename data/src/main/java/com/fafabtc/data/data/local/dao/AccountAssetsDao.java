package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.fafabtc.data.model.entity.exchange.AccountAssets;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Dao
public abstract class AccountAssetsDao {

    @Insert
    public abstract long insertOne(AccountAssets accountAssets);

    @Update
    public abstract void update(AccountAssets... accountAssets);

    @Delete
    public abstract void delete(AccountAssets... accountAssets);

    @Query("select * from account_assets where state = :state")
    public abstract List<AccountAssets> findByState(String state);

    @Query("select * from account_assets where state = 'CURRENT_ACTIVE' limit 1")
    public abstract AccountAssets findCurrent();

    @Query("select * from account_assets where uuid = :uuid limit 1")
    public abstract AccountAssets findByUUID(String uuid);

    @Query("select * from account_assets")
    public abstract List<AccountAssets> findAll();

    @Transaction
    public void deleteAssets(AccountAssets accountAssets) {
        delete(accountAssets);
        if (accountAssets.getState() == AccountAssets.State.CURRENT_ACTIVE) {
            List<AccountAssets> assetsList = findAll();
            if (assetsList.isEmpty()) {
                return;
            } else {
                AccountAssets assets = assetsList.get(0);
                assets.setState(AccountAssets.State.CURRENT_ACTIVE);
                update(assets);
            }
        }
    }
}

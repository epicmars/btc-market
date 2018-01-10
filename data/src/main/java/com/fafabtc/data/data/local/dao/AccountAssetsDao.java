package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.fafabtc.data.model.entity.exchange.AccountAssets;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Dao
public interface AccountAssetsDao {

    @Insert
    long insertOne(AccountAssets accountAssets);

    @Update
    void update(AccountAssets... accountAssets);

    @Query("select * from account_assets where state = :state")
    List<AccountAssets> findByState(String state);

    @Query("select * from account_assets where state = 'CURRENT_ACTIVE' limit 1")
    AccountAssets findCurrent();

    @Query("select * from account_assets where uuid = :uuid limit 1")
    AccountAssets findByUUID(String uuid);

    @Query("select * from account_assets")
    List<AccountAssets> findAll();
}

package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.fafabtc.data.model.entity.exchange.BalanceAssets;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Deprecated
@Dao
public abstract class BalanceAssetsDao {

    @Insert
    public abstract long insertOne(BalanceAssets balanceAssets);

    @Update
    public abstract void updateOne(BalanceAssets balanceAssets);

    @Query("select * from balance_assets where assets_uuid = :assetsUUID and exchange = :exchange and name = :name limit 1")
    public abstract BalanceAssets find(String assetsUUID, String exchange, String name);

    @Query("select * from balance_assets where assets_uuid = :assetsUUID")
    public abstract List<BalanceAssets> findByAccount(String assetsUUID);

    @Query("select * from balance_assets where assets_uuid = :assetsUUID and exchange = :exchange")
    public abstract List<BalanceAssets> findByAccountAndExchange(String assetsUUID, String exchange);

    @Insert
    public abstract long insertBlockchainAssets(BlockchainAssets blockchainAssets);

    @Update
    public abstract void updateBlockchainAssets(BlockchainAssets blockchainAssets);


}

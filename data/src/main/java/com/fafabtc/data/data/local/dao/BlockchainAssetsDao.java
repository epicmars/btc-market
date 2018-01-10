package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.fafabtc.data.model.entity.exchange.BlockchainAssets;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Dao
public abstract class BlockchainAssetsDao {

    @Insert
    public abstract void insert(BlockchainAssets... blockchainAssets);

    @Update
    public abstract void update(BlockchainAssets... blockchainAssets);

    @Query("select * from blockchain_assets where assets_uuid = :assetsUUID and exchange = :exchange and name = :name limit 1")
    public abstract BlockchainAssets find(String assetsUUID, String exchange, String name);

    @Query("select * from blockchain_assets where assets_uuid = :assetsUUID")
    public abstract List<BlockchainAssets> findByAccount(String assetsUUID);

    @Query("select * from blockchain_assets where assets_uuid = :assetsUUID and name = :name COLLATE NOCASE")
    public abstract List<BlockchainAssets> findByAccountWithName(String assetsUUID, String name);

    @Query("select * from blockchain_assets where assets_uuid = :assetsUUID and exchange = :exchange")
    public abstract List<BlockchainAssets> findByAccountWithExchange(String assetsUUID, String exchange);

}

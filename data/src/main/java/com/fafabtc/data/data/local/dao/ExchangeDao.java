package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.fafabtc.data.model.entity.exchange.Exchange;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Dao
public interface ExchangeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOne(Exchange exchange);

    @Update
    void updateOne(Exchange exchange);

    @Query("select * from exchange where name = :exchangeName limit 1")
    Exchange findByName(String exchangeName);

    @Query("select * from exchange")
    Exchange[] findAll();
}

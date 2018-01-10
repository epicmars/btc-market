package com.fafabtc.binance.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.fafabtc.binance.model.BinancePair;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/13.
 */

@Dao
public interface BinancePairDao {

    @Insert
    void insert(BinancePair... pairs);

    @Insert
    void insertList(List<BinancePair> pairs);

    @Query("select * from pair where symbol = :symbol limit 1")
    BinancePair findBySymbol(final String symbol);

    @Query("select * from pair")
    List<BinancePair> findAll();

}

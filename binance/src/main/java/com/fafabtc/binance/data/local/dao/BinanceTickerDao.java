package com.fafabtc.binance.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fafabtc.binance.model.BinanceTicker;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/13.
 */
@Dao
public interface BinanceTickerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BinanceTicker... tickers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<BinanceTicker> tickerList);

    @Query("with max_timestamp(t) as (select max(timestamp) from ticker) select * from ticker where timestamp in max_timestamp")
    List<BinanceTicker> findLatestTickers();

}

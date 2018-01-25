package com.fafabtc.huobi.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fafabtc.huobi.domain.entity.HuobiTicker;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Dao
public interface HuobiTickerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HuobiTicker... tickers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<HuobiTicker> tickerList);

    @Query("select * from huobi_ticker")
    List<HuobiTicker> findAll();
}

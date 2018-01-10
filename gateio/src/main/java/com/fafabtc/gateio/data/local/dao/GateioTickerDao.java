package com.fafabtc.gateio.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fafabtc.gateio.model.entity.GateioTicker;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/7.
 */

@Dao
public interface GateioTickerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GateioTicker... tickers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<GateioTicker> list);

    @Query("with max_timestamp(t) as (select max(timestamp) from ticker) select * from ticker where timestamp in max_timestamp")
    List<GateioTicker> findLatestTickers();
}

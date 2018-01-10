package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fafabtc.data.model.entity.exchange.Ticker;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/10.
 */
@Dao
public interface TickerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ticker... ticker);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Ticker> tickerList);

    @Query("with t(m) as (select max(timestamp) from ticker where exchange = :exchange) select * from ticker where exchange = :exchange and timestamp in t")
    List<Ticker> getLatest(String exchange);

    @Query("select * from ticker where exchange = :exchange and pair = :pair limit 1")
    Ticker getLatest(String exchange, String pair);

    @Query("with t(m) as (select max(timestamp) from ticker where exchange = :exchange) select * from ticker where exchange = :exchange and base = :base and quote = :quote and timestamp in t limit 1")
    Ticker getLatest(String exchange, String base, String quote);
}

package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fafabtc.data.model.entity.exchange.Pair;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/10.
 */
@Dao
public interface PairDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOne(Pair pair);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Pair> pairList);

    @Query("select distinct base from pair where exchange = :exchange")
    String[] findBases(String exchange);

    @Query("select distinct quote from pair where exchange = :exchange")
    String[] findQuotes(String exchange);

    @Query("with t(base) as (select distinct base from pair) select distinct quote collate nocase from pair where quote not in t")
    String[] findQuotesAsBalance();

    @Query("select count(distinct base) from pair where exchange = :exchange")
    int countBases(String exchange);
}

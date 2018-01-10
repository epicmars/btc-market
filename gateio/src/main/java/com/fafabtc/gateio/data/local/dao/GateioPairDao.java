package com.fafabtc.gateio.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.fafabtc.gateio.model.entity.GateioPair;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/7.
 */

@Dao
public interface GateioPairDao {

    @Insert
    void insert(GateioPair... pairs);

    @Insert
    void insertList(List<GateioPair> list);

    @Query("select * from pair where name = :pairName limit 1")
    GateioPair findByName(String pairName);

    @Query("select distinct quote from pair")
    String[] findQuotes();
}

package com.fafabtc.huobi.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.fafabtc.huobi.domain.entity.HuobiPair;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Dao
public interface HuobiPairDao {

    @Insert
    void insert(HuobiPair... pairs);

    @Insert
    void insertList(List<HuobiPair> pairList);

    @Query("select * from huobi_pair where symbol = :symbol limit 1")
    HuobiPair findBySymbol(String symbol);

    @Query("select * from huobi_pair")
    List<HuobiPair> findAll();
}

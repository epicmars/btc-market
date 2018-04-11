package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.fafabtc.data.model.entity.ExchangeRate;

import java.util.List;

/**
 * Created by jastrelax on 2018/4/7.
 */
@Dao
public interface ExchangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(List<ExchangeRate> exchangeRates);

    @Query("select * from exchange_rate where currency_code = :currencyCode")
    ExchangeRate findByCurrencyCode(String currencyCode);
}

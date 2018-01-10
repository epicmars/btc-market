package com.fafabtc.binance.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.fafabtc.binance.data.local.dao.BinancePairDao;
import com.fafabtc.binance.data.local.dao.BinanceTickerDao;
import com.fafabtc.binance.model.BinancePair;
import com.fafabtc.binance.model.BinanceTicker;
import com.fafabtc.domain.data.local.DateConverter;

/**
 * Created by jastrelax on 2018/1/13.
 */

@Database(entities = {
        BinancePair.class,
        BinanceTicker.class},
        version = 1)
@TypeConverters({DateConverter.class})
public abstract class BinanceDatabase extends RoomDatabase {

    public abstract BinancePairDao binancePairDao();

    public abstract BinanceTickerDao binanceTickerDao();
}

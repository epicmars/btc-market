package com.fafabtc.binance.di;

import android.content.Context;

import com.fafabtc.binance.data.local.BinanceDatabase;
import com.fafabtc.binance.data.local.BinanceDatabaseHelper;
import com.fafabtc.binance.data.local.dao.BinancePairDao;
import com.fafabtc.binance.data.local.dao.BinanceTickerDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jastrelax on 2018/1/13.
 */
@Module
public class BinanceDbModule {

    @Provides
    @Singleton
    public static BinanceDatabase database(Context context) {
        return BinanceDatabaseHelper.binanceDatabase(context);
    }

    @Provides
    @Singleton
    public static BinancePairDao binancePairDao(BinanceDatabase database) {
        return database.binancePairDao();
    }

    @Provides
    @Singleton
    public static BinanceTickerDao binanceTickerDao(BinanceDatabase database) {
        return database.binanceTickerDao();
    }
}

package com.fafabtc.data.di.module;

import android.content.Context;

import com.fafabtc.data.data.local.ExchangeDatabase;
import com.fafabtc.data.data.local.ExchangeDatabaseHelper;
import com.fafabtc.data.data.local.dao.PortfolioDao;
import com.fafabtc.data.data.local.dao.AssetsStatisticsDao;
import com.fafabtc.data.data.local.dao.BlockchainAssetsDao;
import com.fafabtc.data.data.local.dao.ExchangeDao;
import com.fafabtc.data.data.local.dao.ExchangeRateDao;
import com.fafabtc.data.data.local.dao.OrderDao;
import com.fafabtc.data.data.local.dao.PairDao;
import com.fafabtc.data.data.local.dao.TickerDao;
import com.fafabtc.data.data.local.dao.TradeDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Module
public class DataDbModule {

    @Provides
    @Singleton
    public static ExchangeDatabase provideExchangeDatabase(Context context) {
        return ExchangeDatabaseHelper.exchangeDatabase(context);
    }

    @Provides
    @Singleton
    public static PortfolioDao portfolioDao(ExchangeDatabase database) {
        return database.portfolioDao();
    }

    @Provides
    @Singleton
    public static ExchangeDao exchangeDao(ExchangeDatabase database) {
        return database.exchangeDao();
    }

    @Provides
    @Singleton
    public static BlockchainAssetsDao blockchainAssetsDao(ExchangeDatabase database) {
        return database.blockchainAssetsDao();
    }

    @Provides
    @Singleton
    public static OrderDao orderDao(ExchangeDatabase database) {
        return database.orderDao();
    }

    @Provides
    @Singleton
    public static TradeDao tradeDao(ExchangeDatabase database) {
        return database.tradeDao();
    }

    @Provides
    @Singleton
    public static PairDao pairDao(ExchangeDatabase database) {
        return database.pairDao();
    }

    @Provides
    @Singleton
    public static TickerDao tickerDao(ExchangeDatabase database) {
        return database.tickerDao();
    }


    @Provides
    @Singleton
    public static AssetsStatisticsDao assetsStatisticsDao(ExchangeDatabase database) {
        return database.assetsStatisticsDao();
    }

    @Provides
    @Singleton
    public static ExchangeRateDao exchangeRateDao(ExchangeDatabase database) {
        return database.exchangeRateDao();
    }
}

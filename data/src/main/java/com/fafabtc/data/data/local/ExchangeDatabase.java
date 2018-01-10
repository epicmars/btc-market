package com.fafabtc.data.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.fafabtc.data.data.local.converter.AccountAssetsStateConverter;
import com.fafabtc.data.data.local.converter.OrderStateConverter;
import com.fafabtc.data.data.local.converter.OrderTypeConverter;
import com.fafabtc.data.data.local.dao.AccountAssetsDao;
import com.fafabtc.data.data.local.dao.AssetsStatisticsDao;
import com.fafabtc.data.data.local.dao.BlockchainAssetsDao;
import com.fafabtc.data.data.local.dao.ExchangeDao;
import com.fafabtc.data.data.local.dao.OrderDao;
import com.fafabtc.data.data.local.dao.PairDao;
import com.fafabtc.data.data.local.dao.TickerDao;
import com.fafabtc.data.data.local.dao.TradeDao;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.entity.exchange.Order;
import com.fafabtc.data.model.entity.exchange.Pair;
import com.fafabtc.data.model.entity.exchange.Ticker;
import com.fafabtc.data.model.entity.exchange.Trade;
import com.fafabtc.domain.data.local.DateConverter;
import com.fafabtc.domain.data.local.StringArrayConverter;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Database(
        entities = {
                AccountAssets.class,
                BlockchainAssets.class,
                Exchange.class,
                Pair.class,
                Order.class,
                Trade.class,
                Ticker.class,
        },
        version = 1)
@TypeConverters(
        {
                DateConverter.class,
                StringArrayConverter.class,
                AccountAssetsStateConverter.class,
                OrderStateConverter.class,
                OrderTypeConverter.class,
        })
public abstract class ExchangeDatabase extends RoomDatabase {

    public abstract ExchangeDao exchangeDao();

    public abstract AccountAssetsDao accountAssetsDao();

    public abstract BlockchainAssetsDao blockchainAssetsDao();

    public abstract OrderDao orderDao();

    public abstract TradeDao tradeDao();

    public abstract PairDao pairDao();

    public abstract TickerDao tickerDao();

    public abstract AssetsStatisticsDao assetsStatisticsDao();
}

package com.fafabtc.data.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.fafabtc.data.model.entity.exchange.AssetsState;
import com.fafabtc.data.data.local.converter.PortfolioStateConverter;
import com.fafabtc.data.data.local.converter.OrderStateConverter;
import com.fafabtc.data.data.local.converter.OrderTypeConverter;
import com.fafabtc.data.data.local.dao.PortfolioDao;
import com.fafabtc.data.data.local.dao.AssetsStatisticsDao;
import com.fafabtc.data.data.local.dao.BlockchainAssetsDao;
import com.fafabtc.data.data.local.dao.ExchangeDao;
import com.fafabtc.data.data.local.dao.ExchangeRateDao;
import com.fafabtc.data.data.local.dao.OrderDao;
import com.fafabtc.data.data.local.dao.PairDao;
import com.fafabtc.data.data.local.dao.TickerDao;
import com.fafabtc.data.data.local.dao.TradeDao;
import com.fafabtc.data.model.entity.ExchangeRate;
import com.fafabtc.data.model.entity.exchange.ExchangeState;
import com.fafabtc.data.model.entity.exchange.Portfolio;
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
                Portfolio.class,
                BlockchainAssets.class,
                Exchange.class,
                Pair.class,
                Order.class,
                Trade.class,
                Ticker.class,
                ExchangeRate.class,
                AssetsState.class,
                ExchangeState.class
        },
        version = 1)
@TypeConverters(
        {
                DateConverter.class,
                StringArrayConverter.class,
                PortfolioStateConverter.class,
                OrderStateConverter.class,
                OrderTypeConverter.class,
        })
public abstract class ExchangeDatabase extends RoomDatabase {

    public abstract ExchangeDao exchangeDao();

    public abstract PortfolioDao portfolioDao();

    public abstract BlockchainAssetsDao blockchainAssetsDao();

    public abstract OrderDao orderDao();

    public abstract TradeDao tradeDao();

    public abstract PairDao pairDao();

    public abstract TickerDao tickerDao();

    public abstract AssetsStatisticsDao assetsStatisticsDao();

    public abstract ExchangeRateDao exchangeRateDao();
}

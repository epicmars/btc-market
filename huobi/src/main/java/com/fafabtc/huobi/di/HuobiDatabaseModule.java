package com.fafabtc.huobi.di;

import android.content.Context;

import com.fafabtc.huobi.data.local.HuobiDatabase;
import com.fafabtc.huobi.data.local.HuobiDatabaseHelper;
import com.fafabtc.huobi.data.local.dao.HuobiPairDao;
import com.fafabtc.huobi.data.local.dao.HuobiTickerDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Module
public class HuobiDatabaseModule {

    @Provides
    @Singleton
    public HuobiDatabase huobiDatabase(Context context) {
        return HuobiDatabaseHelper.getHuobiDatabase(context);
    }

    @Provides
    @Singleton
    public HuobiPairDao huobiPairDao(HuobiDatabase database) {
        return database.huobiPairDao();
    }

    @Provides
    @Singleton
    public HuobiTickerDao huobiTickerDao(HuobiDatabase database) {
        return database.huobiTickerDao();
    }
}

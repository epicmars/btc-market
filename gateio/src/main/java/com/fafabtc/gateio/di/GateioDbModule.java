package com.fafabtc.gateio.di;

import android.content.Context;

import com.fafabtc.gateio.data.local.GateioDataBaseHelper;
import com.fafabtc.gateio.data.local.GateioDatabase;
import com.fafabtc.gateio.data.local.dao.GateioPairDao;
import com.fafabtc.gateio.data.local.dao.GateioTickerDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Module
public class GateioDbModule {

    @Singleton
    @Provides
    public static GateioDatabase gateioDatabase(Context context) {
        return GateioDataBaseHelper.getDatabase(context);
    }

    @Singleton
    @Provides
    public static GateioPairDao gateioPairDao(GateioDatabase database) {
        return database.gateioPairDao();
    }

    @Singleton
    @Provides
    public static GateioTickerDao gateioTickerDao(GateioDatabase database) {
        return database.gateioTickerDao();
    }
}

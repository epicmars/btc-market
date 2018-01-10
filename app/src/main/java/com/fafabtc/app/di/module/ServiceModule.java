package com.fafabtc.app.di.module;

import com.fafabtc.app.di.scope.ServiceScope;
import com.fafabtc.app.service.MainService;
import com.fafabtc.app.service.SyncService;
import com.fafabtc.app.service.TradeService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Module
public abstract class ServiceModule {

    @ServiceScope
    @ContributesAndroidInjector
    public abstract MainService mainService();

    @ServiceScope
    @ContributesAndroidInjector
    public abstract TradeService tradeService();

    @ServiceScope
    @ContributesAndroidInjector
    public abstract SyncService syncService();

}

package com.fafabtc.app.di.module;

import com.fafabtc.app.di.scope.ActivityScope;
import com.fafabtc.app.ui.TradeActivity;
import com.fafabtc.app.ui.activity.PortfolioCreateActivity;
import com.fafabtc.app.ui.MainActivity;
import com.fafabtc.app.ui.activity.ExchangeAssetsActivity;
import com.fafabtc.app.ui.activity.ExchangeEntryActivity;
import com.fafabtc.app.ui.activity.BalanceDepositActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Module(includes = FragmentModel.class)
public abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    public abstract MainActivity contributeMainActivity();

    @ActivityScope
    @ContributesAndroidInjector
    public abstract TradeActivity gateioTradeActivity();

    @ActivityScope
    @ContributesAndroidInjector
    public abstract PortfolioCreateActivity contributePortfolioCreateActivity();

    @ActivityScope
    @ContributesAndroidInjector
    public abstract ExchangeEntryActivity exchangeEntryActivity();

    @ActivityScope
    @ContributesAndroidInjector
    public abstract ExchangeAssetsActivity exchangeAssetsActivity();

    @ActivityScope
    @ContributesAndroidInjector
    public abstract BalanceDepositActivity rechargeBalanceActivity();
}

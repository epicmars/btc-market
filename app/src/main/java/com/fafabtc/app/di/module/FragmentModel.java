package com.fafabtc.app.di.module;

import com.fafabtc.app.di.scope.FragmentScope;
import com.fafabtc.app.ui.fragment.AccountFragment;
import com.fafabtc.app.ui.fragment.AssetsFragment;
import com.fafabtc.app.ui.fragment.BalanceAssetsFragment;
import com.fafabtc.app.ui.fragment.BlockchainAssetsFragment;
import com.fafabtc.app.ui.fragment.ExchangeEntryFragment;
import com.fafabtc.app.ui.fragment.OrdersFragment;
import com.fafabtc.app.ui.fragment.TickerPagerFragment;
import com.fafabtc.app.ui.fragment.TickersFragment;
import com.fafabtc.app.ui.fragment.TradeBuyFragment;
import com.fafabtc.app.ui.fragment.TradeFragment;
import com.fafabtc.app.ui.fragment.TradeSellFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Module
public abstract class FragmentModel {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract AssetsFragment assetsFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract TickerPagerFragment tickerPagerFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract AccountFragment contributeAccountFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract TickersFragment tickersFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract ExchangeEntryFragment exchangeEntryFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract TradeFragment tradeFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract TradeBuyFragment tradeBuyFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract TradeSellFragment tradeSellFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract OrdersFragment ordersFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract BalanceAssetsFragment balanceAssetsFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract BlockchainAssetsFragment blockchainAssetsFragment();
}

package com.fafabtc.app.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.fafabtc.app.vm.PortfolioCreateViewModel;
import com.fafabtc.app.vm.PortfolioListViewModel;
import com.fafabtc.app.vm.PortfolioViewModel;
import com.fafabtc.app.vm.AssetsViewModel;
import com.fafabtc.app.vm.BalanceAssetsViewModel;
import com.fafabtc.app.vm.BalanceDepositViewModel;
import com.fafabtc.app.vm.BlockchainAssetsViewModel;
import com.fafabtc.app.vm.ExchangeAssetsViewModel;
import com.fafabtc.app.vm.ExchangeEntryViewModel;
import com.fafabtc.app.vm.GateioTradeViewModel;
import com.fafabtc.app.vm.MainViewModel;
import com.fafabtc.app.vm.OrdersViewModel;
import com.fafabtc.app.vm.TickersViewModel;
import com.fafabtc.app.vm.TradeBuyViewModel;
import com.fafabtc.app.vm.TradeSellViewModel;
import com.fafabtc.app.vm.TradeViewModel;
import com.fafabtc.app.vm.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel mainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AssetsViewModel.class)
    abstract ViewModel assetsViewModel(AssetsViewModel assetsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PortfolioCreateViewModel.class)
    abstract ViewModel portfolioCreateViewModel(PortfolioCreateViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PortfolioViewModel.class)
    abstract ViewModel accountViewModel(PortfolioViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeEntryViewModel.class)
    abstract ViewModel exchangeEntryViewModel(ExchangeEntryViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ExchangeAssetsViewModel.class)
    abstract ViewModel exchangeAssetsViewModel(ExchangeAssetsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BalanceDepositViewModel.class)
    abstract ViewModel rechargeBalanceViewModel(BalanceDepositViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GateioTradeViewModel.class)
    abstract ViewModel gateioTradeViewModel(GateioTradeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TradeViewModel.class)
    abstract ViewModel tradeViewModel(TradeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TradeBuyViewModel.class)
    abstract ViewModel tradeBuyViewModel(TradeBuyViewModel tradeBuyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TradeSellViewModel.class)
    abstract ViewModel tradeSellViewModel(TradeSellViewModel tradeSellViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TickersViewModel.class)
    abstract ViewModel tickersViewModel(TickersViewModel tickersViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OrdersViewModel.class)
    abstract ViewModel ordersViewModel(OrdersViewModel ordersViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BalanceAssetsViewModel.class)
    abstract ViewModel balanceAssetsViewModel(BalanceAssetsViewModel balanceAssetsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BlockchainAssetsViewModel.class)
    abstract ViewModel blockchainAssetsViewModel(BlockchainAssetsViewModel blockchainAssetsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PortfolioListViewModel.class)
    abstract ViewModel portfolioListViewModel(PortfolioListViewModel portfolioListViewModel);

    @Binds
    abstract ViewModelProvider.Factory viewModelFactory(ViewModelFactory viewModelFactory);
}

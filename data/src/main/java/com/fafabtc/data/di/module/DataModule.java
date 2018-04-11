package com.fafabtc.data.di.module;

import com.fafabtc.binance.di.BinanceModule;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.data.repo.AssetsStatisticsRepo;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.data.repo.DataRepo;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.data.repo.ExchangeRateRepo;
import com.fafabtc.data.data.repo.ExchangeRepo;
import com.fafabtc.data.data.repo.OrderRepo;
import com.fafabtc.data.data.repo.PairRepo;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.data.data.repo.impl.AccountAssetsRepository;
import com.fafabtc.data.data.repo.impl.AssetsStatisticsRepository;
import com.fafabtc.data.data.repo.impl.BlockchainAssetsRepository;
import com.fafabtc.data.data.repo.impl.DataRepository;
import com.fafabtc.data.data.repo.impl.ExchangeAssetsRepository;
import com.fafabtc.data.data.repo.impl.ExchangeRateRepository;
import com.fafabtc.data.data.repo.impl.ExchangeRepository;
import com.fafabtc.data.data.repo.impl.OrderRepository;
import com.fafabtc.data.data.repo.impl.PairRepository;
import com.fafabtc.data.data.repo.impl.TickerRepository;
import com.fafabtc.gateio.di.GateioModule;
import com.fafabtc.huobi.di.HuobiModule;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Module(includes = {
        DataDbModule.class,
        DataApiModule.class,
        GateioModule.class,
        BinanceModule.class,
        HuobiModule.class
})
public abstract class DataModule {

    @Binds
    @Singleton
    abstract DataRepo dataRepo(DataRepository repository);

    @Binds
    @Singleton
    public abstract AccountAssetsRepo accountAssetsRepo(AccountAssetsRepository repository);

    @Binds
    @Singleton
    public abstract ExchangeRepo exchangeRepo(ExchangeRepository repository);

    @Binds
    @Singleton
    public abstract ExchangeAssetsRepo exchangeAssetsRepo(ExchangeAssetsRepository repository);

    @Binds
    @Singleton
    public abstract BlockchainAssetsRepo blockchainAssetsRepo(BlockchainAssetsRepository repository);

    @Binds
    @Singleton
    public abstract TickerRepo tickerRepo(TickerRepository repository);

    @Binds
    @Singleton
    public abstract AssetsStatisticsRepo assetsStatisticsRepo(AssetsStatisticsRepository repository);

    @Binds
    @Singleton
    public abstract OrderRepo orderRepo(OrderRepository repository);

    @Binds
    @Singleton
    public abstract PairRepo pairRepo(PairRepository repository);

    @Binds
    @Singleton
    public abstract ExchangeRateRepo exchangeRateRepo(ExchangeRateRepository repository);

}

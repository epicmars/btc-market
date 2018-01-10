package com.fafabtc.binance.di;

import com.fafabtc.binance.data.repo.BinancePairRepo;
import com.fafabtc.binance.data.repo.BinanceRepo;
import com.fafabtc.binance.data.repo.BinanceTickerRepo;
import com.fafabtc.binance.data.repo.impl.BinancePairRepository;
import com.fafabtc.binance.data.repo.impl.BinanceRepository;
import com.fafabtc.binance.data.repo.impl.BinanceTickerRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Created by jastrelax on 2018/1/13.
 */
@Module(includes = {
        BinanceApiModule.class,
        BinanceDbModule.class})
public abstract class BinanceModule {

    @Binds
    @Singleton
    public abstract BinanceRepo binanceRepo(BinanceRepository repository);

    @Binds
    @Singleton
    public abstract BinancePairRepo binancePairRepo(BinancePairRepository repository);

    @Binds
    @Singleton
    public abstract BinanceTickerRepo binanceTickerRepo(BinanceTickerRepository repository);


}

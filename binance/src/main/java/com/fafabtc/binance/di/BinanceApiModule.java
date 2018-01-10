package com.fafabtc.binance.di;

import com.fafabtc.binance.data.remote.api.BinanceApi;
import com.fafabtc.binance.data.remote.networks.BinanceHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jastrelax on 2018/1/13.
 */

@Module
public class BinanceApiModule {

    @Provides
    @Singleton
    public BinanceApi binanceApi() {
        return BinanceHttpClient.api();
    }
}

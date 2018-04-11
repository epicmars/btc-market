package com.fafabtc.data.di.module;

import com.fafabtc.data.data.remote.DataHttpClient;
import com.fafabtc.data.data.remote.api.BlockchainInfoApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jastrelax on 2018/4/7.
 */
@Module
public class DataApiModule {

    @Provides
    @Singleton
    public static BlockchainInfoApi blockchainInfoApi() {
        return DataHttpClient.instance().retrofit(BlockchainInfoApi.EXCHANGE_RATE_URL).create(BlockchainInfoApi.class);
    }
}

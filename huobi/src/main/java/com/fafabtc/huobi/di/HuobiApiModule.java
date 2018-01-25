package com.fafabtc.huobi.di;

import com.fafabtc.huobi.data.remote.api.HuobiApi;
import com.fafabtc.huobi.data.remote.networks.HuobiHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Module
public class HuobiApiModule {

    @Provides
    @Singleton
    HuobiApi huobiApi() {
        return HuobiHttpClient.api();
    }

}

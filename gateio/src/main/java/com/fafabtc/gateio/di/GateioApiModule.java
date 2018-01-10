package com.fafabtc.gateio.di;

import com.fafabtc.gateio.data.remote.api.GateioApi;
import com.fafabtc.gateio.data.remote.networks.GateioHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Module
public class GateioApiModule {

    @Provides
    @Singleton
    public static GateioApi gateioApi() {
        return GateioHttpClient.api();
    }
}

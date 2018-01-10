package com.fafabtc.gateio.data.remote.networks;

import com.fafabtc.gateio.BuildConfig;
import com.fafabtc.gateio.data.remote.api.GateioApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jastrelax on 2018/1/7.
 */

public class GateioHttpClient {

    private static volatile GateioApi api;

    private OkHttpClient httpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    private Retrofit retrofit() {
        return new Retrofit.Builder()
                .client(httpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.GATEIO_BASE_URL)
                .build();
    }

    public static GateioApi api() {
        if (api == null) {
            synchronized (GateioHttpClient.class) {
                if (api == null) {
                    api = new GateioHttpClient().retrofit().create(GateioApi.class);
                }
            }
        }
        return api;
    }
}

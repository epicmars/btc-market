package com.fafabtc.binance.data.remote.networks;

import com.fafabtc.binance.BuildConfig;
import com.fafabtc.binance.data.remote.api.BinanceApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jastrelax on 2018/1/13.
 */

public class BinanceHttpClient {

    private static volatile BinanceApi api;

    private OkHttpClient httpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    private Retrofit retrofit() {
        return new Retrofit.Builder()
                .client(httpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BINANCE_BASE_URL)
                .build();
    }

    public static BinanceApi api() {
        if (api == null) {
            synchronized (BinanceHttpClient.class) {
                if (api == null) {
                    api = new BinanceHttpClient().retrofit().create(BinanceApi.class);
                }
            }
        }
        return api;
    }
}

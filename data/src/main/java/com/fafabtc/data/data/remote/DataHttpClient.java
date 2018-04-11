package com.fafabtc.data.data.remote;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jastrelax on 2018/4/7.
 */
public class DataHttpClient {

    private static volatile DataHttpClient INSTANCE;

    public static DataHttpClient instance() {
        if (INSTANCE == null) {
            synchronized (DataHttpClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataHttpClient();
                }
            }
        }
        return INSTANCE;
    }

    public Retrofit retrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

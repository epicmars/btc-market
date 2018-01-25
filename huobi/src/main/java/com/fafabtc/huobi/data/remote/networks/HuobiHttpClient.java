package com.fafabtc.huobi.data.remote.networks;

import com.fafabtc.huobi.BuildConfig;
import com.fafabtc.huobi.data.remote.api.HuobiApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jastrelax on 2018/1/25.
 */

public class HuobiHttpClient {

    private static volatile HuobiApi api;

    private static OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HuobiRequestInterceptor())
                .build();
    }

    private static Retrofit retrofit() {
        return new Retrofit.Builder()
                .client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.HUOBI_API_BASE_URL)
                .build();
    }

    public static HuobiApi api() {
        if (api == null) {
            synchronized (HuobiHttpClient.class) {
                if (api == null) {
                    api = retrofit().create(HuobiApi.class);
                }
            }
        }
        return api;
    }
}

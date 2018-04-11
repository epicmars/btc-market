package com.fafabtc.data.data.remote.api;

import com.google.gson.JsonObject;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by jastrelax on 2018/4/7.
 */
public interface BlockchainInfoApi {

    String EXCHANGE_RATE_URL = "https://blockchain.info/";

    @GET("ticker")
    Single<JsonObject> exchangeRate();
}

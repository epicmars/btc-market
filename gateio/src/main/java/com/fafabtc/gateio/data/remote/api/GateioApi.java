package com.fafabtc.gateio.data.remote.api;

import com.fafabtc.gateio.data.remote.dto.GateioMarketList;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * Gate.io Api.
 *
 * Created by jastrelax on 2018/1/7.
 */

public interface GateioApi {

    @GET("1/pairs")
    Single<String[]> pairs();

    @GET("1/marketinfo")
    Single<JsonObject> marketInfo();

    @GET("1/marketlist")
    Single<GateioMarketList> marketList();

    @GET("1/tickers")
    Single<JsonObject> tickers();

}

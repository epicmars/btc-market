package com.fafabtc.binance.data.remote.api;

import com.fafabtc.binance.data.remote.dto.BinanceExchangeInfo;
import com.fafabtc.binance.data.remote.dto.BinanceTicker24hr;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jastrelax on 2018/1/13.
 */

public interface BinanceApi {

    @GET("api/v1/exchangeInfo")
    Single<BinanceExchangeInfo> exchangeInfo();

    @GET("api/v1/ticker/24hr")
    Single<List<BinanceTicker24hr>> tickers24hr(@Query("symbol") String symbol);

}

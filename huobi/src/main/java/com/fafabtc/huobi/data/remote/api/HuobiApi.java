package com.fafabtc.huobi.data.remote.api;

import com.fafabtc.huobi.data.remote.dto.HuobiCommonSymbols;
import com.fafabtc.huobi.data.remote.dto.HuobiMarketDetailMerged;
import com.fafabtc.huobi.data.remote.dto.HuobiMarketDetail;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jastrelax on 2018/1/25.
 */

public interface HuobiApi {

    @GET("v1/common/symbols")
    Single<HuobiCommonSymbols> commonSymbols();

    @GET("market/detail/merged")
    Single<HuobiMarketDetailMerged> huobiTicker(@Query("symbol") String symbol);

    @GET("market/detail")
    Single<HuobiMarketDetail> huobiTicker24Hr(@Query("symbol") String symbol);
}

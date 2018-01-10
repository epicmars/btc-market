package com.fafabtc.gateio;

import com.fafabtc.common.json.GsonHelper;
import com.fafabtc.gateio.data.remote.api.GateioApi;
import com.fafabtc.gateio.data.remote.dto.GateioMarketList;
import com.fafabtc.gateio.data.remote.networks.GateioHttpClient;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by jastrelax on 2018/1/7.
 */

public class GateioApiTest {

    GateioApi api;

    @Before
    public void init() {
        api = GateioHttpClient.api();
    }

    @Test
    public void testPairs() {
        api.pairs().subscribe(new SingleObserver<String[]>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(String[] strings) {
                System.out.println(strings);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testMarketInfo() {
        api.marketInfo().subscribe(new SingleObserver<JsonObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(JsonObject jsonObject) {
                System.out.println(jsonObject.toString());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testMarketList() {
        api.marketList().subscribe(new SingleObserver<GateioMarketList>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(GateioMarketList gateioMarketList) {
                System.out.println(GsonHelper.gson().toJson(gateioMarketList));
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testTickers() throws Exception{
        api.tickers().subscribe(new SingleObserver<JsonObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(JsonObject jsonObject) {
                System.out.println(jsonObject.toString());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }
}

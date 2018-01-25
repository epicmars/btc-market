package com.fafabtc.huobi;

import com.fafabtc.huobi.data.remote.api.HuobiApi;
import com.fafabtc.huobi.data.remote.dto.HuobiCommonSymbols;
import com.fafabtc.huobi.data.remote.dto.HuobiMarketDetailMerged;
import com.fafabtc.huobi.data.remote.dto.HuobiMarketDetail;
import com.fafabtc.huobi.data.remote.networks.HuobiHttpClient;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by jastrelax on 2018/1/25.
 */

public class HuobiApiTest {

    HuobiApi api;

    @Before
    public void init() {
        api = HuobiHttpClient.api();
    }

    @Test
    public void testHuobiCommonSymbols() {
        api.commonSymbols()
                .subscribe(new SingleObserver<HuobiCommonSymbols>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(HuobiCommonSymbols huobiCommonSymbols) {
                        System.out.println(huobiCommonSymbols.getStatus());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    public void testHuobiTicker() {
        String symbol = "btcusdt";
        api.huobiTicker(symbol)
                .subscribe(new SingleObserver<HuobiMarketDetailMerged>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(HuobiMarketDetailMerged huobiTicker) {
                        System.out.println(huobiTicker.getStatus());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    public void testHuobiTicker24Hr() {
        String symbol = "ethusdt";
        api.huobiTicker24Hr(symbol)
                .subscribe(new SingleObserver<HuobiMarketDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(HuobiMarketDetail huobiTicker24Hr) {
                        System.out.println(huobiTicker24Hr.getStatus());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }


}

package com.fafabtc.data;

import com.fafabtc.data.data.remote.DataHttpClient;
import com.fafabtc.data.data.remote.api.BlockchainInfoApi;
import com.google.gson.JsonObject;

import org.junit.Test;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by jastrelax on 2018/4/7.
 */
public class BlockchainInfoApiTest {

    @Test
    public void testGetExchangeRate() {
        BlockchainInfoApi api = DataHttpClient.instance().retrofit(BlockchainInfoApi.EXCHANGE_RATE_URL)
                .create(BlockchainInfoApi.class);

        api.exchangeRate()
                .subscribe(new SingleObserver<JsonObject>() {
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

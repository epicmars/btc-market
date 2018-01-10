package com.fafabtc.binance;

import com.fafabtc.binance.data.remote.api.BinanceApi;
import com.fafabtc.binance.data.remote.dto.BinanceExchangeInfo;
import com.fafabtc.binance.data.remote.dto.BinanceTicker24hr;
import com.fafabtc.binance.data.remote.networks.BinanceHttpClient;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by jastrelax on 2018/1/13.
 */

public class BinanceApiTest {

    BinanceApi api;

    @Before
    public void setup() {
        api = BinanceHttpClient.api();
    }

    @Test
    public void testExchangeInfo() {
        api.exchangeInfo()
                .subscribe(new SingleObserver<BinanceExchangeInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BinanceExchangeInfo binanceExchangeInfo) {
                        System.out.println(binanceExchangeInfo.getTimezone());
                        for (BinanceExchangeInfo.SymbolsBean s : binanceExchangeInfo.getSymbols()) {
                            System.out.println(s.getSymbol() + "-"
                                    + s.getBaseAsset() + "-"
                                    + s.getQuoteAsset());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    public void testBinanceTickers() {
        api.tickers24hr(null)
                .subscribe(new SingleObserver<List<BinanceTicker24hr>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<BinanceTicker24hr> binanceTicker24hrs) {
                        for (BinanceTicker24hr ticker24hr : binanceTicker24hrs) {
                            System.out.println(ticker24hr.getSymbol());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

}

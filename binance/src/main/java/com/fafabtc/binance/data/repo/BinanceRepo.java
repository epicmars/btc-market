package com.fafabtc.binance.data.repo;

import com.fafabtc.binance.model.BinancePair;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/13.
 */

public interface BinanceRepo {

    String BINANCE_EXCHANGE = "binance";

    Single<List<BinancePair>> initBinanceExchange();
}

package com.fafabtc.binance.data.repo;

import com.fafabtc.binance.model.BinanceTicker;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/13.
 */

public interface BinanceTickerRepo {

    Single<List<BinanceTicker>> getCachedBinanceTickers();

    Single<List<BinanceTicker>> getLatestBinanceTickers(Date timestamp);
}

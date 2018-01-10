package com.fafabtc.binance.data.repo;

import com.fafabtc.binance.model.BinancePair;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/13.
 */

public interface BinancePairRepo {

    Single<List<BinancePair>> getCachedBinancePairs();

    Single<BinancePair> getBinancePair(String symbol);

    Single<List<BinancePair>> getLatestBinancePairs();
}

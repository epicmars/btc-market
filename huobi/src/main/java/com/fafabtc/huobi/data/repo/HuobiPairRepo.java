package com.fafabtc.huobi.data.repo;

import com.fafabtc.huobi.domain.entity.HuobiPair;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/25.
 */

public interface HuobiPairRepo {

    Single<List<HuobiPair>> getLatestPairs();

    Single<List<HuobiPair>> getPairsFromDb();

    Single<HuobiPair> getPairFromDb(String symbol);
}

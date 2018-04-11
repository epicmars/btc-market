package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.Pair;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/10.
 */

public interface PairRepo {

    Completable save(Pair pair);

    Single<Integer> baseCount(String exchange);

}

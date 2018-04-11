package com.fafabtc.data.data.repo;

import io.reactivex.Completable;

/**
 * Created by jastrelax on 2018/1/9.
 */

public interface DataRepo {

    Completable initiate();

    Completable refreshTickers();

    Completable refreshTickers(String exchange);
}

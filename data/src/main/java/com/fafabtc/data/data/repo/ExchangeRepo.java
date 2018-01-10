package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.Exchange;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface ExchangeRepo {

    Completable init();

    Completable initGateio();

    Completable initBinance();

    Completable save(Exchange exchange);

    Single<Exchange[]> getExchanges();
}

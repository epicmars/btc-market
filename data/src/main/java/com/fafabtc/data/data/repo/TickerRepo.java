package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.Ticker;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/10.
 */

public interface TickerRepo {

    Completable save(Ticker... ticker);

    Completable save(List<Ticker> tickerList);

    Single<Ticker> getLatestTickerFromCache(String exchange, String base, String quote);

    Single<List<Ticker>> getLatestTickerListFromCache(String exchange);

    Single<List<Ticker>> getLatestTickers(String exchange, Date timestamp);

    Single<List<Ticker>> getAllLatestTickers();
}

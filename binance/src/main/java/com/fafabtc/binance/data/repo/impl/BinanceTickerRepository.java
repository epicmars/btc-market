package com.fafabtc.binance.data.repo.impl;

import com.fafabtc.binance.data.local.dao.BinancePairDao;
import com.fafabtc.binance.data.local.dao.BinanceTickerDao;
import com.fafabtc.binance.data.remote.api.BinanceApi;
import com.fafabtc.binance.data.remote.dto.BinanceTicker24hr;
import com.fafabtc.binance.data.remote.mapper.BinanceTicker24hrMapper;
import com.fafabtc.binance.data.repo.BinanceTickerRepo;
import com.fafabtc.binance.model.BinanceTicker;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jastrelax on 2018/1/13.
 */
@Singleton
public class BinanceTickerRepository implements BinanceTickerRepo {

    @Inject
    BinanceApi api;

    @Inject
    BinanceTickerDao dao;

    @Inject
    BinancePairDao pairDao;

    @Inject
    public BinanceTickerRepository() {
    }

    @Override
    public Single<List<BinanceTicker>> getCachedBinanceTickers() {
        return Single.fromCallable(new Callable<List<BinanceTicker>>() {
            @Override
            public List<BinanceTicker> call() throws Exception {
                return dao.findLatestTickers();
            }
        });
    }

    @Override
    public Single<List<BinanceTicker>> getLatestBinanceTickers(final Date timestamp) {
        return api.tickers24hr(null)
                .flattenAsObservable(new Function<List<BinanceTicker24hr>, Iterable<BinanceTicker24hr>>() {
                    @Override
                    public Iterable<BinanceTicker24hr> apply(List<BinanceTicker24hr> binanceTicker24hrs) throws Exception {
                        return binanceTicker24hrs;
                    }
                })
                .filter(new Predicate<BinanceTicker24hr>() {
                    @Override
                    public boolean test(BinanceTicker24hr binanceTicker24hr) throws Exception {
                        return !binanceTicker24hr.getSymbol().equalsIgnoreCase("123456");
                    }
                })
                .map(BinanceTicker24hrMapper.MAPPER)
                .map(new Function<BinanceTicker, BinanceTicker>() {
                    @Override
                    public BinanceTicker apply(BinanceTicker binanceTicker) throws Exception {
                        binanceTicker.setTimestamp(timestamp);
                        return binanceTicker;
                    }
                })
                .toList();
    }
}

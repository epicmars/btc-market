package com.fafabtc.gateio.data.repo.impl;

import com.fafabtc.gateio.data.local.dao.GateioTickerDao;
import com.fafabtc.gateio.data.remote.api.GateioApi;
import com.fafabtc.gateio.data.remote.dto.GateioTickers;
import com.fafabtc.gateio.data.remote.mapper.GateioTickersMapper;
import com.fafabtc.gateio.data.repo.GateioTickerRepo;
import com.fafabtc.gateio.model.entity.GateioTicker;
import com.google.gson.JsonObject;

import org.reactivestreams.Publisher;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/7.
 */

public class GateioTickerRepository implements GateioTickerRepo {

    @Inject
    GateioApi mApi;
    @Inject
    GateioTickerDao mDao;

    @Inject
    public GateioTickerRepository() {
    }

    @Override
    public Single<List<GateioTicker>> getTickers() {
        return Single.fromCallable(new Callable<List<GateioTicker>>() {
            @Override
            public List<GateioTicker> call() throws Exception {
                return mDao.findLatestTickers();
            }
        });
    }

    @Override
    public Flowable<List<GateioTicker>> getLatestTickers(final Date timestamp) {
        return mApi.tickers()
                .flattenAsFlowable(new Function<JsonObject, Iterable<GateioTickers.PairTicker>>() {
                    @Override
                    public Iterable<GateioTickers.PairTicker> apply(JsonObject jsonObject) throws Exception {
                        return GateioTickersMapper.MAPPER.from(jsonObject).getTickers();
                    }
                })
                .map(new Function<GateioTickers.PairTicker, GateioTicker>() {
                    @Override
                    public GateioTicker apply(GateioTickers.PairTicker pairTicker) throws Exception {
                        return pairTicker.toTicker(timestamp);
                    }
                })
                .flatMap(new Function<GateioTicker, Publisher<GateioTicker>>() {
                    @Override
                    public Publisher<GateioTicker> apply(final GateioTicker gateioTicker) throws Exception {
                        return Flowable.fromCallable(new Callable<GateioTicker>() {
                            @Override
                            public GateioTicker call() throws Exception {
                                mDao.insert(gateioTicker);
                                return gateioTicker;
                            }
                        }).onErrorReturnItem(gateioTicker);
                    }
                })
                .toList()
                .toFlowable();
    }
}

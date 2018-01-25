package com.fafabtc.huobi.data.repo.impl;

import com.fafabtc.huobi.data.local.dao.HuobiPairDao;
import com.fafabtc.huobi.data.local.dao.HuobiTickerDao;
import com.fafabtc.huobi.data.remote.api.HuobiApi;
import com.fafabtc.huobi.data.remote.dto.HuobiMarketDetailMerged;
import com.fafabtc.huobi.data.remote.mapper.HuobiMarketDetailMergedMapper;
import com.fafabtc.huobi.data.repo.HuobiTickerRepo;
import com.fafabtc.huobi.domain.entity.HuobiPair;
import com.fafabtc.huobi.domain.entity.HuobiTicker;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Singleton
public class HuobiTickerRepository implements HuobiTickerRepo {

    @Inject
    HuobiApi api;

    @Inject
    HuobiTickerDao tickerDao;

    @Inject
    HuobiPairDao pairDao;

    @Inject
    public HuobiTickerRepository() {
    }

    @Override
    public Single<List<HuobiTicker>> getLatestTickers(final Date timestamp) {

        return Single
                .fromCallable(new Callable<List<HuobiPair>>() {
                    @Override
                    public List<HuobiPair> call() throws Exception {
                        return pairDao.findAll();
                    }
                })
                .flattenAsObservable(new Function<List<HuobiPair>, Iterable<HuobiPair>>() {
                    @Override
                    public Iterable<HuobiPair> apply(List<HuobiPair> pairList) throws Exception {
                        return pairList;
                    }
                })
                .flatMapMaybe(new Function<HuobiPair, MaybeSource<HuobiTicker>>() {
                    @Override
                    public MaybeSource<HuobiTicker> apply(final HuobiPair huobiPair) throws Exception {
                        return Maybe.fromSingle(api.huobiTicker(huobiPair.getSymbol()))
                                .map(new Function<HuobiMarketDetailMerged, HuobiTicker>() {
                                    @Override
                                    public HuobiTicker apply(HuobiMarketDetailMerged huobiMarketDetailMerged) throws Exception {
                                        HuobiTicker ticker = HuobiMarketDetailMergedMapper.MAPPER.apply(huobiMarketDetailMerged);
                                        return ticker;
                                    }
                                })
                                .flatMap(new Function<HuobiTicker, MaybeSource<HuobiTicker>>() {
                                    @Override
                                    public MaybeSource<HuobiTicker> apply(final HuobiTicker huobiTicker) throws Exception {
                                        return Maybe.fromCallable(new Callable<HuobiTicker>() {
                                            @Override
                                            public HuobiTicker call() throws Exception {
                                                huobiTicker.setSymbol(huobiPair.getSymbol());
                                                huobiTicker.setTimestamp(timestamp);
                                                tickerDao.insert(huobiTicker);
                                                return huobiTicker;
                                            }
                                        });
                                    }
                                }).onErrorComplete();
                    }
                })
                .toList();
    }
}

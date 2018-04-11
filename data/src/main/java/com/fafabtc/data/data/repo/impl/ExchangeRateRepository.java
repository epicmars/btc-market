package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.local.dao.ExchangeRateDao;
import com.fafabtc.data.data.remote.api.BlockchainInfoApi;
import com.fafabtc.data.data.remote.mapper.ExchangeRateMapper;
import com.fafabtc.data.data.repo.ExchangeRateRepo;
import com.fafabtc.data.model.entity.ExchangeRate;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by jastrelax on 2018/4/7.
 */
public class ExchangeRateRepository implements ExchangeRateRepo {

    @Inject
    BlockchainInfoApi blockchainInfoApi;

    @Inject
    ExchangeRateDao exchangeRateDao;

    @Inject
    public ExchangeRateRepository() {
    }

    @Override
    public Single<List<ExchangeRate>> getExchangeRate() {
        return blockchainInfoApi.exchangeRate()
                .map(ExchangeRateMapper.MAPPER)
                .doOnSuccess(new Consumer<List<ExchangeRate>>() {
                    @Override
                    public void accept(List<ExchangeRate> exchangeRates) throws Exception {
                        Date timestamp = new Date();
                        for (ExchangeRate rate : exchangeRates) {
                            rate.setTimestamp(timestamp);
                        }
                        exchangeRateDao.save(exchangeRates);
                    }
                });
    }

    @Override
    public Single<Double> getUsdAgainstCnyRate() {
        return Single.zip(getExchangeRate("USD"),
                getExchangeRate("CNY"),
                new BiFunction<ExchangeRate, ExchangeRate, Double>() {
                    @Override
                    public Double apply(ExchangeRate usdExchangeRate, ExchangeRate cnyExchangeRate) throws Exception {
                        return cnyExchangeRate.getLast() / usdExchangeRate.getLast();
                    }
                });
    }

    private Single<ExchangeRate> getExchangeRate(final String currencyCode) {
        return Single.fromCallable(new Callable<ExchangeRate>() {
            @Override
            public ExchangeRate call() throws Exception {
                return exchangeRateDao.findByCurrencyCode(currencyCode);
            }
        });
    }
}

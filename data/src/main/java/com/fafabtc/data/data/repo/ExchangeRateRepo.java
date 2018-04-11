package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.ExchangeRate;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/4/7.
 */
public interface ExchangeRateRepo {

    Single<List<ExchangeRate>> getExchangeRate();

    Single<Double> getUsdAgainstCnyRate();
}

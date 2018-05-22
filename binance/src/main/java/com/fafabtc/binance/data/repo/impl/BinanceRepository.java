package com.fafabtc.binance.data.repo.impl;

import com.fafabtc.binance.data.repo.BinancePairRepo;
import com.fafabtc.binance.data.repo.BinanceRepo;
import com.fafabtc.binance.model.BinancePair;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/13.
 */
@Singleton
public class BinanceRepository implements BinanceRepo{

    @Inject
    BinancePairRepo pairRepo;

    @Inject
    public BinanceRepository() {
    }

    @Override
    public Single<List<BinancePair>> initBinanceExchange() {
        return pairRepo.getLatestBinancePairs();
    }
}

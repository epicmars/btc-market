package com.fafabtc.huobi.data.repo.impl;

import com.fafabtc.huobi.data.repo.HuobiPairRepo;
import com.fafabtc.huobi.data.repo.HuobiRepo;
import com.fafabtc.huobi.domain.entity.HuobiPair;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Singleton
public class HuobiRepository implements HuobiRepo{

    @Inject
    HuobiPairRepo huobiPairRepo;

    @Inject
    public HuobiRepository() {
    }

    @Override
    public Single<List<HuobiPair>> initHuobiExchange() {
        return huobiPairRepo.getLatestPairs();
    }
}

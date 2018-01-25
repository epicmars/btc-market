package com.fafabtc.huobi.data.repo;

import com.fafabtc.huobi.domain.entity.HuobiPair;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/25.
 */

public interface HuobiRepo {
    String HUOBI_EXCHANGE = "huobi";

    Single<List<HuobiPair>> init();

}

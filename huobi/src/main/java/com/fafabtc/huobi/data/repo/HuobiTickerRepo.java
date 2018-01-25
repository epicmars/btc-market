package com.fafabtc.huobi.data.repo;

import com.fafabtc.huobi.domain.entity.HuobiTicker;

import java.util.Date;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/25.
 */

public interface HuobiTickerRepo {

    Single<List<HuobiTicker>> getLatestTickers(Date timestamp);
}

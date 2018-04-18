package com.fafabtc.gateio.data.repo;

import com.fafabtc.gateio.model.entity.GateioTicker;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/7.
 */

public interface GateioTickerRepo {

    Single<List<GateioTicker>> getTickers();

    Single<List<GateioTicker>> getLatestTickers(final Date timestamp);
}

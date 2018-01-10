package com.fafabtc.gateio.data.repo;

import com.fafabtc.gateio.model.entity.GateioPair;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface GateioRepo {

    String GATEIO_EXCHANGE = "gateio";

    Single<List<GateioPair>> init();
}

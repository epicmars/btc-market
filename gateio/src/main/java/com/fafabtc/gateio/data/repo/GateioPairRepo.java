package com.fafabtc.gateio.data.repo;


import com.fafabtc.gateio.model.entity.GateioPair;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/7.
 */

public interface GateioPairRepo {

    Single<List<GateioPair>> getGateioPairs();

}

package com.fafabtc.gateio.data.repo.impl;

import com.fafabtc.gateio.data.repo.GateioPairRepo;
import com.fafabtc.gateio.data.repo.GateioRepo;
import com.fafabtc.gateio.model.entity.GateioPair;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Singleton
public class GateioRepository implements GateioRepo {

    @Inject
    GateioPairRepo gateioPairRepo;

    @Inject
    public GateioRepository() {
    }

    @Override
    public Single<List<GateioPair>> initGateioExchange() {
        return gateioPairRepo.getGateioPairs();
    }
}

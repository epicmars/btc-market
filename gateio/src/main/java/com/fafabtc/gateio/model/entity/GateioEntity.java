package com.fafabtc.gateio.model.entity;

import com.fafabtc.base.data.local.BaseEntity;
import com.fafabtc.gateio.data.repo.GateioRepo;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class GateioEntity extends BaseEntity {

    // package control
    String exchange = GateioRepo.GATEIO_EXCHANGE;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}

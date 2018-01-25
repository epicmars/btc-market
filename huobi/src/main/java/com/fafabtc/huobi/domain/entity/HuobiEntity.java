package com.fafabtc.huobi.domain.entity;

import com.fafabtc.domain.data.local.BaseEntity;
import com.fafabtc.huobi.data.repo.HuobiRepo;

/**
 * Created by jastrelax on 2018/1/25.
 */

public class HuobiEntity extends BaseEntity {

    public String exchange = HuobiRepo.HUOBI_EXCHANGE;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}

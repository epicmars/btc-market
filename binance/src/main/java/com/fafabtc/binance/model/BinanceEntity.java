package com.fafabtc.binance.model;

import com.fafabtc.binance.data.repo.BinanceRepo;
import com.fafabtc.domain.data.local.BaseEntity;

/**
 * Created by jastrelax on 2018/1/13.
 */

public class BinanceEntity extends BaseEntity{

    public String exchange = BinanceRepo.BINANCE_EXCHANGE;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}

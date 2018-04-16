package com.fafabtc.data.model.vo;

import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.data.model.entity.exchange.Exchange;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class ExchangeEntry {

    private Portfolio portfolio;

    private Exchange exchange;

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
}

package com.fafabtc.data.model.vo;

import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class ExchangeEntry {

    private AccountAssets accountAssets;

    private Exchange exchange;

    public AccountAssets getAccountAssets() {
        return accountAssets;
    }

    public void setAccountAssets(AccountAssets accountAssets) {
        this.accountAssets = accountAssets;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
}

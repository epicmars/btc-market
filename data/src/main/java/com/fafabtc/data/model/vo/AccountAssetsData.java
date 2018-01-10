package com.fafabtc.data.model.vo;

import com.fafabtc.data.model.entity.exchange.AccountAssets;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class AccountAssetsData {

    private AccountAssets accountAssets;

    private List<ExchangeAssets> exchangeAssets;

    public AccountAssets getAccountAssets() {
        return accountAssets;
    }

    public void setAccountAssets(AccountAssets accountAssets) {
        this.accountAssets = accountAssets;
    }

    public List<ExchangeAssets> getExchangeAssets() {
        return exchangeAssets;
    }

    public void setExchangeAssets(List<ExchangeAssets> exchangeAssets) {
        this.exchangeAssets = exchangeAssets;
    }
}

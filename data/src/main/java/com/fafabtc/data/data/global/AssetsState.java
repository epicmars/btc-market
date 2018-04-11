package com.fafabtc.data.data.global;

import java.util.Date;

/**
 * Created by jastrelax on 2018/1/18.
 */

public class AssetsState {

    private String exchange;
    // Update time of tickers.
    private Date updateTime;

    private Boolean isAssetsInitialized;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getAssetsInitialized() {
        return isAssetsInitialized;
    }

    public void setAssetsInitialized(Boolean assetsInitialized) {
        isAssetsInitialized = assetsInitialized;
    }
}

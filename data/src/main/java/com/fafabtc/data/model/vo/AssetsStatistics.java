package com.fafabtc.data.model.vo;

import android.arch.persistence.room.ColumnInfo;

/**
 * a template table for assets caculation.
 *
 * assets_uuid|exchange|name|available|locked|last|quote|quote_last|usdt_last|usdt_avg|usdt_max
 * Created by jastrelax on 2018/1/10.
 */

public class AssetsStatistics {

    @ColumnInfo(name = "assets_uuid")
    private String assetsUUID;
    private String exchange;
    private String name;
    private double available;
    private double locked;
    private double last;
    private String quote;
    @ColumnInfo(name = "quote_last")
    private double quoteLast;
    @ColumnInfo(name = "usdt_last")
    private double usdtLast;
    @ColumnInfo(name = "usdt_avg")
    private double usdtAvg;
    @ColumnInfo(name = "base_volume")
    private double baseVolumeMax;

    public String getAssetsUUID() {
        return assetsUUID;
    }

    public void setAssetsUUID(String assetsUUID) {
        this.assetsUUID = assetsUUID;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getLocked() {
        return locked;
    }

    public void setLocked(double locked) {
        this.locked = locked;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public double getQuoteLast() {
        return quoteLast;
    }

    public void setQuoteLast(double quoteLast) {
        this.quoteLast = quoteLast;
    }

    public double getUsdtLast() {
        return usdtLast;
    }

    public void setUsdtLast(double usdtLast) {
        this.usdtLast = usdtLast;
    }

    public double getUsdtAvg() {
        return usdtAvg;
    }

    public void setUsdtAvg(double usdtAvg) {
        this.usdtAvg = usdtAvg;
    }

    public double getBaseVolumeMax() {
        return baseVolumeMax;
    }

    public void setBaseVolumeMax(double baseVolumeMax) {
        this.baseVolumeMax = baseVolumeMax;
    }
}

package com.fafabtc.gateio.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

/**
 * Created by jastrelax on 2018/1/7.
 */

@Entity(tableName = "market_list")
public class MarketList extends GateioEntity {

    private Long no;
    private String symbol;
    private String name;
    @ColumnInfo(name = "name_en")
    private String nameEn;
    @ColumnInfo(name = "name_cn")
    private String nameCn;
    @ColumnInfo(name = "pair")
    private String pair;
    @ColumnInfo(name = "rate")
    private String rate;
    @ColumnInfo(name = "vol_a")
    private double volA;
    @ColumnInfo(name = "vol_b")
    private String volB;
    @ColumnInfo(name = "curr_a")
    private String currA;
    @ColumnInfo(name = "curr_b")
    private String currB;
    @ColumnInfo(name = "curr_suffix")
    private String currSuffix;
    @ColumnInfo(name = "rate_percent")
    private String ratePercent;
    private String trend;
    private Long supply;
    private String marketcap;
    @Ignore
    private Object plot;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public double getVolA() {
        return volA;
    }

    public void setVolA(double volA) {
        this.volA = volA;
    }

    public String getVolB() {
        return volB;
    }

    public void setVolB(String volB) {
        this.volB = volB;
    }

    public String getCurrA() {
        return currA;
    }

    public void setCurrA(String currA) {
        this.currA = currA;
    }

    public String getCurrB() {
        return currB;
    }

    public void setCurrB(String currB) {
        this.currB = currB;
    }

    public String getCurrSuffix() {
        return currSuffix;
    }

    public void setCurrSuffix(String currSuffix) {
        this.currSuffix = currSuffix;
    }

    public String getRatePercent() {
        return ratePercent;
    }

    public void setRatePercent(String ratePercent) {
        this.ratePercent = ratePercent;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public Long getSupply() {
        return supply;
    }

    public void setSupply(Long supply) {
        this.supply = supply;
    }

    public String getMarketcap() {
        return marketcap;
    }

    public void setMarketcap(String marketcap) {
        this.marketcap = marketcap;
    }

    public Object getPlot() {
        return plot;
    }

    public void setPlot(Object plot) {
        this.plot = plot;
    }

}

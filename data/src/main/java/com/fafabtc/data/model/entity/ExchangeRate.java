package com.fafabtc.data.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.fafabtc.domain.data.local.BaseEntity;

/**
 * Created by jastrelax on 2018/4/7.
 */
@Entity(tableName = "exchange_rate", indices = @Index(value = "currency_code", unique = true))
public class ExchangeRate extends BaseEntity{

    @ColumnInfo(name = "currency_code")
    private String currencyCode;
    private Double delay;
    private Double last;
    private Double buy;
    private Double sell;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getDelay() {
        return delay;
    }

    public void setDelay(Double delay) {
        this.delay = delay;
    }

    public Double getLast() {
        return last;
    }

    public void setLast(Double last) {
        this.last = last;
    }

    public Double getBuy() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }

    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }
}

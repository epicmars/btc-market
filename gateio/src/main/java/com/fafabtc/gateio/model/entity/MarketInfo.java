package com.fafabtc.gateio.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

/**
 * Created by jastrelax on 2018/1/7.
 */

@Entity(tableName = "market_info")
public class MarketInfo extends GateioEntity{

    private String pair;

    @ColumnInfo(name = "decimal_places")
    private Long decimalPlaces;

    @ColumnInfo(name = "min_amount")
    private double minAmount;

    @ColumnInfo(name = "fee")
    private double fee;

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public Long getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Long decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }


}

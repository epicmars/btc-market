package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.fafabtc.domain.data.local.BaseEntity;


/**
 * 区块链资产交易，一项交易在一个交易所完成，并且属于一项账户资产。
 * 即多个交易可以属于同一交易所，或者同一项资产。
 * Created by jastrelax on 2018/1/7.
 */
@Entity(tableName = "trade")
public class Trade extends BaseEntity {

    @ColumnInfo(name = "exchange")
    private String exchange;

    @ColumnInfo(name = "assets_uuid")
    private String assetsUuid;

    /**
     * "buy" or "sell"
     */
    private String type;

    /**
     * block chain assets name.
     */
    private String name;

    /**
     * "usdt", "btc", "eth" or else
     */
    private String quoteName;

    private double quantity;

    private double price;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getAssetsUuid() {
        return assetsUuid;
    }

    public void setAssetsUuid(String assetsUuid) {
        this.assetsUuid = assetsUuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuoteName() {
        return quoteName;
    }

    public void setQuoteName(String quoteName) {
        this.quoteName = quoteName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

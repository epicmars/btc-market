package com.fafabtc.binance.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

/**
 * Created by jastrelax on 2018/1/13.
 */
@Entity(tableName = "pair", indices = @Index(
        value = "symbol",
        unique = true
))
public class BinancePair extends BinanceEntity{

    /**
     * 区块链交易对名称。
     */
    private String symbol;

    /**
     * 交易区块链名称。
     */
    private String base;

    /**
     * 兑换区块链名称。
     */
    private String quote;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}

package com.fafabtc.huobi.domain.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Entity(tableName = "huobi_pair", indices = @Index(value = {"symbol"}, unique = true))
public class HuobiPair extends HuobiEntity{

    private String symbol;

    private String base;

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

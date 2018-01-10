package com.fafabtc.gateio.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

/**
 * Created by jastrelax on 2018/1/7.
 */

@Entity(tableName = "pair", indices = {@Index(value = "name", unique = true)})
public class GateioPair extends GateioEntity{

    /**
     * 对名称。
     */
    private String name;

    /**
     * 交易区块链名称。
     */
    private String base;

    /**
     * 兑换区块链币种名。
     */
    private String quote;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

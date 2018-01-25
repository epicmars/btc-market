package com.fafabtc.huobi.domain.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Entity(tableName = "huobi_ticker", indices = @Index(value = {"symbol"}, unique = true))
public class HuobiTicker extends HuobiEntity{

    private String symbol;
    @ColumnInfo(name = "amount")
    private double amount;
    @ColumnInfo(name = "open")
    private double open;
    @ColumnInfo(name = "close")
    private double close;
    @ColumnInfo(name = "high")
    private double high;
    @ColumnInfo(name = "kline_id")
    private long klineId;
    @ColumnInfo(name = "count")
    private long count;
    @ColumnInfo(name = "low")
    private double low;
    @ColumnInfo(name = "version")
    private int version;
    @ColumnInfo(name = "vol")
    private double vol;
    @ColumnInfo(name = "ask")
    private double ask;
    @ColumnInfo(name = "bid")
    private double bid;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public long getKlineId() {
        return klineId;
    }

    public void setKlineId(long klineId) {
        this.klineId = klineId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }
}

package com.fafabtc.binance.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

/**
 * Created by jastrelax on 2018/1/13.
 */
@Entity(tableName = "ticker",
        indices = @Index(
                value = "symbol",
                unique = true
        ),
        inheritSuperIndices = true)
public class BinanceTicker extends BinanceEntity {

    private String symbol;

    @ColumnInfo(name = "last_price")
    private double lastPrice;

    @ColumnInfo(name = "price_change_percent")
    private double priceChangePercent;

    @ColumnInfo(name = "ask_price")
    private double askPrice;

    @ColumnInfo(name = "bid_price")
    private double bidPrice;

    @ColumnInfo(name = "high_price")
    private double highPrice;

    @ColumnInfo(name = "low_price")
    private double lowPrice;

    @ColumnInfo(name = "volume")
    private double volume;

    @ColumnInfo(name = "quote_volume")
    private double quoteVolume;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(double priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(double quoteVolume) {
        this.quoteVolume = quoteVolume;
    }
}

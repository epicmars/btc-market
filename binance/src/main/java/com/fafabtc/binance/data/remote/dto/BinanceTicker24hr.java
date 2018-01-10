package com.fafabtc.binance.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jastrelax on 2018/1/13.
 */

public class BinanceTicker24hr {

    /**
     * symbol : ETHBTC
     * priceChange : 0.00578300
     * priceChangePercent : 6.748
     * weightedAvgPrice : 0.08949417
     * prevClosePrice : 0.08570000
     * lastPrice : 0.09148300
     * lastQty : 5.31200000
     * bidPrice : 0.09148300
     * bidQty : 11.54600000
     * askPrice : 0.09156100
     * askQty : 0.00100000
     * openPrice : 0.08570000
     * highPrice : 0.09201600
     * lowPrice : 0.08500000
     * volume : 195570.14100000
     * quoteVolume : 17502.38777567
     * openTime : 1515716792704
     * closeTime : 1515803192704
     * firstId : 19861373
     * lastId : 20197077
     * count : 335705
     */

    @SerializedName("symbol")
    private String symbol;
    @SerializedName("priceChange")
    private String priceChange;
    @SerializedName("priceChangePercent")
    private String priceChangePercent;
    @SerializedName("weightedAvgPrice")
    private String weightedAvgPrice;
    @SerializedName("prevClosePrice")
    private String prevClosePrice;
    @SerializedName("lastPrice")
    private String lastPrice;
    @SerializedName("lastQty")
    private String lastQty;
    @SerializedName("bidPrice")
    private String bidPrice;
    @SerializedName("bidQty")
    private String bidQty;
    @SerializedName("askPrice")
    private String askPrice;
    @SerializedName("askQty")
    private String askQty;
    @SerializedName("openPrice")
    private String openPrice;
    @SerializedName("highPrice")
    private String highPrice;
    @SerializedName("lowPrice")
    private String lowPrice;
    @SerializedName("volume")
    private String volume;
    @SerializedName("quoteVolume")
    private String quoteVolume;
    @SerializedName("openTime")
    private long openTime;
    @SerializedName("closeTime")
    private long closeTime;
    @SerializedName("firstId")
    private int firstId;
    @SerializedName("lastId")
    private int lastId;
    @SerializedName("count")
    private int count;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(String priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public String getWeightedAvgPrice() {
        return weightedAvgPrice;
    }

    public void setWeightedAvgPrice(String weightedAvgPrice) {
        this.weightedAvgPrice = weightedAvgPrice;
    }

    public String getPrevClosePrice() {
        return prevClosePrice;
    }

    public void setPrevClosePrice(String prevClosePrice) {
        this.prevClosePrice = prevClosePrice;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getLastQty() {
        return lastQty;
    }

    public void setLastQty(String lastQty) {
        this.lastQty = lastQty;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getBidQty() {
        return bidQty;
    }

    public void setBidQty(String bidQty) {
        this.bidQty = bidQty;
    }

    public String getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

    public String getAskQty() {
        return askQty;
    }

    public void setAskQty(String askQty) {
        this.askQty = askQty;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(String quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(long openTime) {
        this.openTime = openTime;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public int getFirstId() {
        return firstId;
    }

    public void setFirstId(int firstId) {
        this.firstId = firstId;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

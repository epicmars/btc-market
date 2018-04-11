package com.fafabtc.gateio.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * 交易行情。
 * <p>
 * <pre>
 *    baseVolume: 交易量
 *    high24hr:24小时最高价
 *    highestBid:买方最高价
 *    last:最新成交价
 *    low24hr:24小时最低价
 *    lowestAsk:卖方最低价
 *    percentChange:涨跌百分比
 *    quoteVolume: 兑换货币交易量
 * </pre>
 * <p>
 * Created by jastrelax on 2018/1/7.
 */

@Entity(tableName = "ticker",
        indices = @Index(value = "pair", unique = true))
public class GateioTicker extends GateioEntity implements Parcelable {

    private String pair;
    private String result;
    private double last;
    @ColumnInfo(name = "lowest_ask")
    private double lowestAsk;
    @ColumnInfo(name = "highest_bid")
    private double highestBid;
    @ColumnInfo(name = "percent_change")
    private double percentChange;
    @ColumnInfo(name = "base_volume")
    private double baseVolume;
    @ColumnInfo(name = "quote_volume")
    private double quoteVolume;
    @ColumnInfo(name = "high24hr")
    private double high24hr;
    @ColumnInfo(name = "low24hr")
    private double low24hr;

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getLowestAsk() {
        return lowestAsk;
    }

    public void setLowestAsk(double lowestAsk) {
        this.lowestAsk = lowestAsk;
    }

    public double getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(double highestBid) {
        this.highestBid = highestBid;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }

    public double getBaseVolume() {
        return baseVolume;
    }

    public void setBaseVolume(double baseVolume) {
        this.baseVolume = baseVolume;
    }

    public double getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(double quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public double getHigh24hr() {
        return high24hr;
    }

    public void setHigh24hr(double high24hr) {
        this.high24hr = high24hr;
    }

    public double getLow24hr() {
        return low24hr;
    }

    public void setLow24hr(double low24hr) {
        this.low24hr = low24hr;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pair);
        dest.writeString(this.result);
        dest.writeDouble(this.last);
        dest.writeDouble(this.lowestAsk);
        dest.writeDouble(this.highestBid);
        dest.writeDouble(this.percentChange);
        dest.writeDouble(this.baseVolume);
        dest.writeDouble(this.quoteVolume);
        dest.writeDouble(this.high24hr);
        dest.writeDouble(this.low24hr);
        dest.writeString(this.exchange);
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
    }

    public GateioTicker() {
    }

    protected GateioTicker(Parcel in) {
        this.pair = in.readString();
        this.result = in.readString();
        this.last = in.readDouble();
        this.lowestAsk = in.readDouble();
        this.highestBid = in.readDouble();
        this.percentChange = in.readDouble();
        this.baseVolume = in.readDouble();
        this.quoteVolume = in.readDouble();
        this.high24hr = in.readDouble();
        this.low24hr = in.readDouble();
        this.exchange = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
    }

    public static final Creator<GateioTicker> CREATOR = new Creator<GateioTicker>() {
        @Override
        public GateioTicker createFromParcel(Parcel source) {
            return new GateioTicker(source);
        }

        @Override
        public GateioTicker[] newArray(int size) {
            return new GateioTicker[size];
        }
    };
}

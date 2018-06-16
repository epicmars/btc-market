package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;

import com.fafabtc.base.data.local.BaseEntity;

import java.util.Date;

/**
 * 根据项目依赖结构，Ticker由各交易所数据生成。
 * Created by jastrelax on 2018/1/9.
 */
@Entity(tableName = "ticker",
        inheritSuperIndices = true,
        indices = @Index(value = {"exchange", "pair"}, unique = true))
public class Ticker extends BaseEntity implements Parcelable {

    @ColumnInfo(index = true)
    private String exchange;

    private String pair;

    @ColumnInfo(index = true)
    private String base;

    @ColumnInfo(index = true)
    private String quote;

    @ColumnInfo(index = true)
    private double last;

    private double ask;

    private double bid;

    private double percentChange;

    @ColumnInfo(name = "base_volume")
    private double baseVolume;
    @ColumnInfo(name = "quote_volume")
    private double quoteVolume;
    @ColumnInfo(name = "high24hr")
    private double high24hr;
    @ColumnInfo(name = "low24hr")
    private double low24hr;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
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

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
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

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }

    public Ticker() {
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
        dest.writeString(this.exchange);
        dest.writeString(this.pair);
        dest.writeString(this.base);
        dest.writeString(this.quote);
        dest.writeDouble(this.last);
        dest.writeDouble(this.ask);
        dest.writeDouble(this.bid);
        dest.writeDouble(this.percentChange);
        dest.writeDouble(this.baseVolume);
        dest.writeDouble(this.quoteVolume);
        dest.writeDouble(this.high24hr);
        dest.writeDouble(this.low24hr);
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
    }

    protected Ticker(Parcel in) {
        this.exchange = in.readString();
        this.pair = in.readString();
        this.base = in.readString();
        this.quote = in.readString();
        this.last = in.readDouble();
        this.ask = in.readDouble();
        this.bid = in.readDouble();
        this.percentChange = in.readDouble();
        this.baseVolume = in.readDouble();
        this.quoteVolume = in.readDouble();
        this.high24hr = in.readDouble();
        this.low24hr = in.readDouble();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
    }

    public static final Creator<Ticker> CREATOR = new Creator<Ticker>() {
        @Override
        public Ticker createFromParcel(Parcel source) {
            return new Ticker(source);
        }

        @Override
        public Ticker[] newArray(int size) {
            return new Ticker[size];
        }
    };
}

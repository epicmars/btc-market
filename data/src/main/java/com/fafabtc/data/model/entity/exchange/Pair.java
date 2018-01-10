package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;

import com.fafabtc.domain.data.local.BaseEntity;

import java.util.Date;

/**
 * Created by jastrelax on 2018/1/10.
 */
@Entity(tableName = "pair", indices = @Index(value = {"exchange", "pair"}, unique = true))
public class Pair extends BaseEntity implements Parcelable{

    private String exchange;

    private String pair;

    private String base;

    private String quote;

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
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
    }

    public Pair() {
    }

    protected Pair(Parcel in) {
        this.exchange = in.readString();
        this.pair = in.readString();
        this.base = in.readString();
        this.quote = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
    }

    public static final Creator<Pair> CREATOR = new Creator<Pair>() {
        @Override
        public Pair createFromParcel(Parcel source) {
            return new Pair(source);
        }

        @Override
        public Pair[] newArray(int size) {
            return new Pair[size];
        }
    };
}

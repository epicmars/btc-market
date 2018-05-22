package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;

import com.fafabtc.domain.data.local.BaseEntity;

import java.util.Date;

/**
 * Created by jastrelax on 2018/5/20.
 */
@Entity(indices = @Index(value = {"exchange"}, unique = true))
public class ExchangeState extends BaseEntity implements Parcelable{

    private String exchange;

    @ColumnInfo(name = "is_exchange_initialized")
    private int isExchangeInitialized;

    // Update time of tickers.
    @ColumnInfo(name = "update_time")
    private Date updateTime;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public int getIsExchangeInitialized() {
        return isExchangeInitialized;
    }

    public void setIsExchangeInitialized(int isExchangeInitialized) {
        this.isExchangeInitialized = isExchangeInitialized;
    }

    public Boolean getExchangeInitialized() {
        return isExchangeInitialized == 1;
    }

    public void setExchangeInitialized(Boolean exchangeInitialized) {
        isExchangeInitialized = exchangeInitialized? 1 : 0;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.exchange);
        dest.writeInt(this.isExchangeInitialized);
        dest.writeLong(this.updateTime != null ? this.updateTime.getTime() : -1);
    }

    public ExchangeState() {
    }

    protected ExchangeState(Parcel in) {
        this.exchange = in.readString();
        this.isExchangeInitialized = in.readInt();
        long tmpUpdateTime = in.readLong();
        this.updateTime = tmpUpdateTime == -1 ? null : new Date(tmpUpdateTime);
    }

    public static final Creator<ExchangeState> CREATOR = new Creator<ExchangeState>() {
        @Override
        public ExchangeState createFromParcel(Parcel source) {
            return new ExchangeState(source);
        }

        @Override
        public ExchangeState[] newArray(int size) {
            return new ExchangeState[size];
        }
    };
}

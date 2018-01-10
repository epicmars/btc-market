package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fafabtc.domain.data.local.BaseEntity;

import java.util.Date;

/**
 * 余额资产，用于交易区块链资产的法定货币。它也属于一个交易所和一个资产组合。
 *
 * Created by jastrelax on 2018/1/7.
 */

@Deprecated
@Entity(tableName = "balance_assets", primaryKeys = {"assets_uuid", "exchange", "name"})
public class BalanceAssets extends BaseEntity implements Parcelable{

    @ColumnInfo(name = "exchange")
    @NonNull
    private String exchange;

    @ColumnInfo(name = "assets_uuid")
    @NonNull
    private String assetsUUID;

    /**
     * 资产名称，一般作为定价或兑换。
     */
    @NonNull
    private String currency;

    /**
     * 本金。
     */
    private double principle;

    /**
     * 余额。
     */
    private double available;

    /**
     * 冻结金额。
     */
    private double locked;

    public String getExchange() {
        return exchange;
    }

    public double getPrinciple() {
        return principle;
    }

    public void setExchange(@NonNull String exchange) {
        this.exchange = exchange;
    }

    @NonNull
    public String getAssetsUUID() {
        return assetsUUID;
    }

    public void setAssetsUUID(@NonNull String assetsUUID) {
        this.assetsUUID = assetsUUID;
    }

    @NonNull
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(@NonNull String currency) {
        this.currency = currency;
    }

    public void setPrinciple(double principle) {
        this.principle = principle;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getLocked() {
        return locked;
    }

    public void setLocked(double locked) {
        this.locked = locked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.exchange);
        dest.writeString(this.assetsUUID);
        dest.writeString(this.currency);
        dest.writeDouble(this.principle);
        dest.writeDouble(this.available);
        dest.writeDouble(this.locked);
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
    }

    public BalanceAssets() {
    }

    protected BalanceAssets(Parcel in) {
        this.exchange = in.readString();
        this.assetsUUID = in.readString();
        this.currency = in.readString();
        this.principle = in.readDouble();
        this.available = in.readDouble();
        this.locked = in.readDouble();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
    }

    public static final Creator<BalanceAssets> CREATOR = new Creator<BalanceAssets>() {
        @Override
        public BalanceAssets createFromParcel(Parcel source) {
            return new BalanceAssets(source);
        }

        @Override
        public BalanceAssets[] newArray(int size) {
            return new BalanceAssets[size];
        }
    };
}

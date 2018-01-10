package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fafabtc.domain.data.local.BaseEntity;

import java.util.Date;


/**
 * 区块链资产。它是由交易而产生和改变的区块链资产，也属于一个交易所以及一个账户资产。
 *
 * 作为定价的区块链资产有USDT, BTC, ETH等。
 * Created by jastrelax on 2018/1/7.
 */
@Entity(tableName = "blockchain_assets", primaryKeys = {"assets_uuid", "exchange", "name"})
public class BlockchainAssets extends BaseEntity implements Parcelable{

    @ColumnInfo(name = "assets_uuid")
    @NonNull
    private String assetsUUID;

    @ColumnInfo(name = "exchange")
    @NonNull
    private String exchange;

    @NonNull
    private String name;

    private double principle;

    private double available;

    private double locked;

    public double getPrinciple() {
        return principle;
    }

    public void setPrinciple(double principle) {
        this.principle = principle;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getAssetsUUID() {
        return assetsUUID;
    }

    public void setAssetsUUID(String assetsUUID) {
        this.assetsUUID = assetsUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeString(this.assetsUUID);
        dest.writeString(this.exchange);
        dest.writeString(this.name);
        dest.writeDouble(this.principle);
        dest.writeDouble(this.available);
        dest.writeDouble(this.locked);
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
    }

    public BlockchainAssets() {
    }

    protected BlockchainAssets(Parcel in) {
        this.assetsUUID = in.readString();
        this.exchange = in.readString();
        this.name = in.readString();
        this.principle = in.readDouble();
        this.available = in.readDouble();
        this.locked = in.readDouble();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
    }

    public static final Creator<BlockchainAssets> CREATOR = new Creator<BlockchainAssets>() {
        @Override
        public BlockchainAssets createFromParcel(Parcel source) {
            return new BlockchainAssets(source);
        }

        @Override
        public BlockchainAssets[] newArray(int size) {
            return new BlockchainAssets[size];
        }
    };
}

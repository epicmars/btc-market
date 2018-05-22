package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;

import com.fafabtc.domain.data.local.BaseEntity;

import java.util.Date;

/**
 * 交易所。
 * Created by jastrelax on 2018/1/7.
 */
@Entity(tableName = "exchange", indices = @Index(value = "name",unique = true))
public class Exchange extends BaseEntity implements Parcelable{
    /**
     * English name or abbr, it's unique.
     */
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    private String name;

    @ColumnInfo(name = "name_cn")
    private String nameCN;

    @ColumnInfo(name = "commission_rate")
    private double commissionRate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Exchange() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.nameCN);
        dest.writeDouble(this.commissionRate);
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
    }

    protected Exchange(Parcel in) {
        this.name = in.readString();
        this.nameCN = in.readString();
        this.commissionRate = in.readDouble();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
    }

    public static final Creator<Exchange> CREATOR = new Creator<Exchange>() {
        @Override
        public Exchange createFromParcel(Parcel source) {
            return new Exchange(source);
        }

        @Override
        public Exchange[] newArray(int size) {
            return new Exchange[size];
        }
    };
}

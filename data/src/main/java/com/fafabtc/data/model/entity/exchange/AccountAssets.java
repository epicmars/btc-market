package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fafabtc.domain.data.local.BaseEntity;

import java.util.Date;

/**
 * 账户资产。可以具有多个模拟的资产组合，它们之间相互独立。
 * Created by jastrelax on 2018/1/7.
 */

@Entity(tableName = "account_assets",
        indices = {@Index(value = "name", unique = true),
        @Index(value = "uuid", unique = true)})
public class AccountAssets extends BaseEntity implements Parcelable{

    public static final String DEFAULT_NAME = "我的资产";

    public static enum State {
        ACTIVE,
        CURRENT_ACTIVE,
        DELETED
    }

    private String name = DEFAULT_NAME;

    @NonNull
    private String uuid;

    /**
     * Caculate balance based on balance assets and blockchain assets.
     * Pricing using USDT for now.
     */
    private double balance;

    private double locked;

    private State state = State.ACTIVE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getLocked() {
        return locked;
    }

    public void setLocked(double locked) {
        this.locked = locked;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public AccountAssets() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.uuid);
        dest.writeDouble(this.balance);
        dest.writeDouble(this.locked);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
    }

    protected AccountAssets(Parcel in) {
        this.name = in.readString();
        this.uuid = in.readString();
        this.balance = in.readDouble();
        this.locked = in.readDouble();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : State.values()[tmpState];
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
    }

    public static final Creator<AccountAssets> CREATOR = new Creator<AccountAssets>() {
        @Override
        public AccountAssets createFromParcel(Parcel source) {
            return new AccountAssets(source);
        }

        @Override
        public AccountAssets[] newArray(int size) {
            return new AccountAssets[size];
        }
    };
}

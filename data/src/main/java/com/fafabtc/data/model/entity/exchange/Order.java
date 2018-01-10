package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fafabtc.domain.data.local.BaseEntity;

import java.util.Date;

/**
 * 区块链资产交易订单，一项订单在一个交易所完成，并且属于一项账户资产。
 * 即多个订单可以属于同一交易所，或者同一项资产。
 * Created by jastrelax on 2018/1/9.
 */
@Entity(tableName = "orders")
public class Order extends BaseEntity implements Parcelable{

    public enum Type {
        BUY,
        SELL
    }

    public enum State {
        PENDING,
        DONE,
        CANCELLED
    }

    @ColumnInfo(name = "assets_uuid")
    private String assetsUUID;

    @ColumnInfo(name = "exchange")
    private String exchange;

    @PrimaryKey
    @NonNull
    private String uuid;

    /**
     * "buy" or "sell"
     */
    private Type type;

    private State state = State.PENDING;

    private String pair;

    /**
     * block chain assets name.
     */
    private String base;

    /**
     * "usdt", "btc", "eth" or else
     */
    @ColumnInfo(name = "quote")
    private String quote;

    private double quantity;

    private double price;

    private double commission;

    @ColumnInfo(name = "commission_asset")
    private String commissionAsset;

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCommissionAsset() {
        return commissionAsset;
    }

    public void setCommissionAsset(String commissionAsset) {
        this.commissionAsset = commissionAsset;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Order() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.assetsUUID);
        dest.writeString(this.exchange);
        dest.writeString(this.uuid);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeString(this.pair);
        dest.writeString(this.base);
        dest.writeString(this.quote);
        dest.writeDouble(this.quantity);
        dest.writeDouble(this.price);
        dest.writeDouble(this.commission);
        dest.writeString(this.commissionAsset);
        dest.writeValue(this.id);
        dest.writeLong(this.timestamp != null ? this.timestamp.getTime() : -1);
    }

    protected Order(Parcel in) {
        this.assetsUUID = in.readString();
        this.exchange = in.readString();
        this.uuid = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : State.values()[tmpState];
        this.pair = in.readString();
        this.base = in.readString();
        this.quote = in.readString();
        this.quantity = in.readDouble();
        this.price = in.readDouble();
        this.commission = in.readDouble();
        this.commissionAsset = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        long tmpTimestamp = in.readLong();
        this.timestamp = tmpTimestamp == -1 ? null : new Date(tmpTimestamp);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}

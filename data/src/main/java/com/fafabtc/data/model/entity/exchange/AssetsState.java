package com.fafabtc.data.model.entity.exchange;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;

import com.fafabtc.base.data.local.BaseEntity;

/**
 * Created by jastrelax on 2018/1/18.
 */

@Entity(indices = @Index(value = {"exchange", "assets_uuid"}, unique = true))
public class AssetsState extends BaseEntity implements Parcelable{

    private String exchange;

    @ColumnInfo(name = "assets_uuid")
    private String assetsUuid;

    @ColumnInfo(name = "is_assets_initialized")
    private int isAssetsInitialized;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public int getIsAssetsInitialized() {
        return isAssetsInitialized;
    }

    public void setIsAssetsInitialized(int isAssetsInitialized) {
        this.isAssetsInitialized = isAssetsInitialized;
    }

    public Boolean getAssetsInitialized() {
        return isAssetsInitialized == 1;
    }

    public void setAssetsInitialized(Boolean assetsInitialized) {
        isAssetsInitialized = assetsInitialized? 1 : 0;
    }

    public String getAssetsUuid() {
        return assetsUuid;
    }

    public void setAssetsUuid(String assetsUuid) {
        this.assetsUuid = assetsUuid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.exchange);
        dest.writeString(this.assetsUuid);
        dest.writeInt(this.isAssetsInitialized);
    }

    public AssetsState() {
    }

    protected AssetsState(Parcel in) {
        this.exchange = in.readString();
        this.assetsUuid = in.readString();
        this.isAssetsInitialized = in.readInt();
    }

    public static final Creator<AssetsState> CREATOR = new Creator<AssetsState>() {
        @Override
        public AssetsState createFromParcel(Parcel source) {
            return new AssetsState(source);
        }

        @Override
        public AssetsState[] newArray(int size) {
            return new AssetsState[size];
        }
    };
}

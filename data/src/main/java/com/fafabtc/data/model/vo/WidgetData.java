package com.fafabtc.data.model.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.fafabtc.data.model.entity.exchange.Portfolio;

public class WidgetData implements Parcelable {
    private Portfolio portfolio;
    private String updateTime;
    private double volume;

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.portfolio, flags);
        dest.writeString(this.updateTime);
        dest.writeDouble(this.volume);
    }

    public WidgetData() {
    }

    protected WidgetData(Parcel in) {
        this.portfolio = in.readParcelable(Portfolio.class.getClassLoader());
        this.updateTime = in.readString();
        this.volume = in.readDouble();
    }

    public static final Creator<WidgetData> CREATOR = new Creator<WidgetData>() {
        @Override
        public WidgetData createFromParcel(Parcel source) {
            return new WidgetData(source);
        }

        @Override
        public WidgetData[] newArray(int size) {
            return new WidgetData[size];
        }
    };
}
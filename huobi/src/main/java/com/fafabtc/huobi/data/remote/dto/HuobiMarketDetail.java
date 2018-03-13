package com.fafabtc.huobi.data.remote.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jastrelax on 2018/1/25.
 */

public class HuobiMarketDetail {


    /**
     * status : ok
     * ch : market.ethusdt.detail
     * ts : 1516868269047
     * tick : {"amount":82354.15706036788,"open":1005.83,"close":1072.9,"high":1114.22,"id":1556852442,"count":103592,"low":985.14,"version":1556852442,"vol":8.688581602875131E7}
     */

    @SerializedName("status")
    private String status;
    @SerializedName("ch")
    private String ch;
    @SerializedName("ts")
    private long ts;
    @SerializedName("tick")
    private TickBean tick;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public TickBean getTick() {
        return tick;
    }

    public void setTick(TickBean tick) {
        this.tick = tick;
    }

    public static class TickBean {
        /**
         * amount : 82354.15706036788
         * open : 1005.83
         * close : 1072.9
         * high : 1114.22
         * id : 1556852442
         * count : 103592
         * low : 985.14
         * version : 1556852442
         * vol : 8.688581602875131E7
         */

        @SerializedName("amount")
        private double amount;
        @SerializedName("open")
        private double open;
        @SerializedName("close")
        private double close;
        @SerializedName("high")
        private double high;
        @SerializedName("id")
        private long id;
        @SerializedName("count")
        private long count;
        @SerializedName("low")
        private double low;
        @SerializedName("version")
        private long version;
        @SerializedName("vol")
        private double vol;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        public double getClose() {
            return close;
        }

        public void setClose(double close) {
            this.close = close;
        }

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
        }

        public double getVol() {
            return vol;
        }

        public void setVol(double vol) {
            this.vol = vol;
        }
    }
}

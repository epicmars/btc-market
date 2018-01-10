package com.fafabtc.gateio.data.remote.dto;

import com.fafabtc.gateio.model.entity.GateioTicker;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * 所有交易行情。
 *
 * <pre>
 *    baseVolume: 交易量
 *    high24hr:24小时最高价
 *    highestBid:买方最高价
 *    last:最新成交价
 *    low24hr:24小时最低价
 *    lowestAsk:卖方最低价
 *    percentChange:涨跌百分比
 *    quoteVolume: 兑换货币交易量
 * </pre>
 *
 * Created by jastrelax on 2018/1/7.
 */

public class GateioTickers {

    /**
     * original -- eth_btc : {"result":"true","last":0.1,"lowestAsk":0.1,"highestBid":"0.00000000","percentChange":0,"baseVolume":0.001,"quoteVolume":0.01,"high24hr":0.1,"low24hr":0.1}
     *
     * transform -- {"pair": "eth_btc", "result":"true","last":0.1,"lowestAsk":0.1,"highestBid":"0.00000000","percentChange":0,"baseVolume":0.001,"quoteVolume":0.01,"high24hr":0.1,"low24hr":0.1}
     */

    @SerializedName("tickers")
    private List<PairTicker> tickers;

    public List<PairTicker> getTickers() {
        return tickers;
    }

    public void setTickers(List<PairTicker> tickers) {
        this.tickers = tickers;
    }

    public static class PairTicker {
        /**
         * result : true
         * last : 0.1
         * lowestAsk : 0.1
         * highestBid : 0.00000000
         * percentChange : 0
         * baseVolume : 0.001
         * quoteVolume : 0.01
         * high24hr : 0.1
         * low24hr : 0.1
         */

        private String pair;
        @SerializedName("result")
        private String result;
        @SerializedName("last")
        private double last;
        @SerializedName("lowestAsk")
        private double lowestAsk;
        @SerializedName("highestBid")
        private double highestBid;
        @SerializedName("percentChange")
        private double percentChange;
        @SerializedName("baseVolume")
        private double baseVolume;
        @SerializedName("quoteVolume")
        private double quoteVolume;
        @SerializedName("high24hr")
        private double high24hr;
        @SerializedName("low24hr")
        private double low24hr;

        public String getPair() {
            return pair;
        }

        public void setPair(String pair) {
            this.pair = pair;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public double getLast() {
            return last;
        }

        public void setLast(double last) {
            this.last = last;
        }

        public double getLowestAsk() {
            return lowestAsk;
        }

        public void setLowestAsk(double lowestAsk) {
            this.lowestAsk = lowestAsk;
        }

        public double getHighestBid() {
            return highestBid;
        }

        public void setHighestBid(double highestBid) {
            this.highestBid = highestBid;
        }

        public double getPercentChange() {
            return percentChange;
        }

        public void setPercentChange(double percentChange) {
            this.percentChange = percentChange;
        }

        public double getBaseVolume() {
            return baseVolume;
        }

        public void setBaseVolume(double baseVolume) {
            this.baseVolume = baseVolume;
        }

        public double getQuoteVolume() {
            return quoteVolume;
        }

        public void setQuoteVolume(double quoteVolume) {
            this.quoteVolume = quoteVolume;
        }

        public double getHigh24hr() {
            return high24hr;
        }

        public void setHigh24hr(double high24hr) {
            this.high24hr = high24hr;
        }

        public double getLow24hr() {
            return low24hr;
        }

        public void setLow24hr(double low24hr) {
            this.low24hr = low24hr;
        }

        public GateioTicker toTicker(Date timestamp) {
            GateioTicker ticker = new GateioTicker();
            ticker.setTimestamp(timestamp);
            ticker.setResult(result);
            ticker.setPair(pair);
            ticker.setBaseVolume(baseVolume);
            ticker.setHigh24hr(high24hr);
            ticker.setHighestBid(highestBid);
            ticker.setLast(last);
            ticker.setLow24hr(low24hr);
            ticker.setLowestAsk(lowestAsk);
            ticker.setPercentChange(percentChange);
            ticker.setQuoteVolume(quoteVolume);
            return ticker;
        }
    }
}

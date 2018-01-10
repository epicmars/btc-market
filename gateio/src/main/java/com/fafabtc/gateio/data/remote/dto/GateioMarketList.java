package com.fafabtc.gateio.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 交易市场详细行情。
 *
 * <pre>
 *    symbol : 币种标识
 *    name: 币种名称
 *    name_en: 英文名称
 *    name_cn: 中文名称
 *    pair: 交易对
 *    rate: 当前价格
 *    vol_a: 被兑换货币交易量
 *    vol_b: 兑换货币交易量
 *    curr_a: 被兑换货币
 *    curr_b: 兑换货币
 *    curr_suffix: 货币类型后缀
 *    rate_percent: 涨跌百分百
 *    trend: 24小时趋势 up涨 down跌
 *    supply: 币种供应量
 *    marketcap: 总市值
 *    plot: 趋势数据
 * </pre>
 * Created by jastrelax on 2018/1/7.
 */

public class GateioMarketList {

    /**
     * result : true
     * data : [{"no":1,"symbol":"LTC","name":"Litecoin","name_en":"Litecoin","name_cn":"莱特币","pair":"ltc_btc","rate":"418.00","vol_a":168120.2,"vol_b":"65,616,561","curr_a":"LTC","curr_b":"BTC","curr_suffix":" BTC","rate_percent":"19.73","trend":"up","supply":25760300,"marketcap":"10,767,805,404","plot":null},{"no":2,"symbol":"ETH","name":"ETH","name_en":"ETH","name_cn":"以太币","pair":"etc_eth","rate":"0.7450","vol_a":6.52273283E7,"vol_b":"51,041,999","curr_a":"etc","curr_b":"eth","curr_suffix":" eth","rate_percent":"-1.84","trend":"up","supply":1050000000,"marketcap":"782,250,000","plot":null}]
     */

    @SerializedName("result")
    private String result;
    @SerializedName("data")
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * no : 1
         * symbol : LTC
         * name : Litecoin
         * name_en : Litecoin
         * name_cn : 莱特币
         * pair : ltc_btc
         * rate : 418.00
         * vol_a : 168120.2
         * vol_b : 65,616,561
         * curr_a : LTC
         * curr_b : BTC
         * curr_suffix :  BTC
         * rate_percent : 19.73
         * trend : up
         * supply : 25760300
         * marketcap : 10,767,805,404
         * plot : null
         */

        @SerializedName("no")
        private Long no;
        @SerializedName("symbol")
        private String symbol;
        @SerializedName("name")
        private String name;
        @SerializedName("name_en")
        private String nameEn;
        @SerializedName("name_cn")
        private String nameCn;
        @SerializedName("pair")
        private String pair;
        @SerializedName("rate")
        private String rate;
        @SerializedName("vol_a")
        private double volA;
        @SerializedName("vol_b")
        private String volB;
        @SerializedName("curr_a")
        private String currA;
        @SerializedName("curr_b")
        private String currB;
        @SerializedName("curr_suffix")
        private String currSuffix;
        @SerializedName("rate_percent")
        private String ratePercent;
        @SerializedName("trend")
        private String trend;
        @SerializedName("supply")
        private Long supply;
        @SerializedName("marketcap")
        private String marketcap;
        @SerializedName("plot")
        private Object plot;

        public Long getNo() {
            return no;
        }

        public void setNo(Long no) {
            this.no = no;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getNameCn() {
            return nameCn;
        }

        public void setNameCn(String nameCn) {
            this.nameCn = nameCn;
        }

        public String getPair() {
            return pair;
        }

        public void setPair(String pair) {
            this.pair = pair;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public double getVolA() {
            return volA;
        }

        public void setVolA(double volA) {
            this.volA = volA;
        }

        public String getVolB() {
            return volB;
        }

        public void setVolB(String volB) {
            this.volB = volB;
        }

        public String getCurrA() {
            return currA;
        }

        public void setCurrA(String currA) {
            this.currA = currA;
        }

        public String getCurrB() {
            return currB;
        }

        public void setCurrB(String currB) {
            this.currB = currB;
        }

        public String getCurrSuffix() {
            return currSuffix;
        }

        public void setCurrSuffix(String currSuffix) {
            this.currSuffix = currSuffix;
        }

        public String getRatePercent() {
            return ratePercent;
        }

        public void setRatePercent(String ratePercent) {
            this.ratePercent = ratePercent;
        }

        public String getTrend() {
            return trend;
        }

        public void setTrend(String trend) {
            this.trend = trend;
        }

        public Long getSupply() {
            return supply;
        }

        public void setSupply(Long supply) {
            this.supply = supply;
        }

        public String getMarketcap() {
            return marketcap;
        }

        public void setMarketcap(String marketcap) {
            this.marketcap = marketcap;
        }

        public Object getPlot() {
            return plot;
        }

        public void setPlot(Object plot) {
            this.plot = plot;
        }
    }
}

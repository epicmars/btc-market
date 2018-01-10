package com.fafabtc.gateio.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 交易市场订单参数
 *
 * <pre>
 *   decimal_places: 价格精度
 *   min_amount : 最小下单量
 *   fee : 交易费
 * </pre>
 *
 * Created by jastrelax on 2018/1/7.
 */

public class GateioMarketInfo {


    /**
     * result : true
     * pairs : [{"eth_btc":{"decimal_places":6,"min_amount":1.0E-4,"fee":0.2}},{"etc_btc":{"decimal_places":6,"min_amount":1.0E-4,"fee":0.2}}]
     */

    @SerializedName("result")
    private String result;
    @SerializedName("pairs")
    private List<PairInfo> pairs;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<PairInfo> getPairs() {
        return pairs;
    }

    public void setPairs(List<PairInfo> pairs) {
        this.pairs = pairs;
    }

    public static class PairInfo {
        /**
         * origin -- eth_btc : {"decimal_places":6,"min_amount":1.0E-4,"fee":0.2}
         * transform --  {"name": "eth_btc", "decimal_places":6,"min_amount":1.0E-4,"fee":0.2}
         */

        @SerializedName("pair")
        private String pair;
        @SerializedName("decimal_places")
        private Long decimalPlaces;
        @SerializedName("min_amount")
        private double minAmount;
        @SerializedName("fee")
        private double fee;

        public String getPair() {
            return pair;
        }

        public void setPair(String pair) {
            this.pair = pair;
        }

        public Long getDecimalPlaces() {
            return decimalPlaces;
        }

        public void setDecimalPlaces(Long decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
        }

        public double getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(double minAmount) {
            this.minAmount = minAmount;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }
    }
}

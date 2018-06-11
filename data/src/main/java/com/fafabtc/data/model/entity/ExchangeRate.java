package com.fafabtc.data.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.fafabtc.domain.data.local.BaseEntity;

/**
 * Exchange rate of different currency.
 *
 * For example, data from https://blockchain.info/ticker:
 * {
 * "USD" : {"15m" : 7507.5565161, "last" : 7507.5565161, "buy" : 7507.5565161, "sell" : 7507.5565161, "symbol" : "$"},
 * "AUD" : {"15m" : 9947.70758030192, "last" : 9947.70758030192, "buy" : 9947.70758030192, "sell" : 9947.70758030192, "symbol" : "$"},
 * "BRL" : {"15m" : 27969.424323400097, "last" : 27969.424323400097, "buy" : 27969.424323400097, "sell" : 27969.424323400097, "symbol" : "R$"},
 * "CAD" : {"15m" : 9744.875925906445, "last" : 9744.875925906445, "buy" : 9744.875925906445, "sell" : 9744.875925906445, "symbol" : "$"},
 * "CHF" : {"15m" : 7434.050030250865, "last" : 7434.050030250865, "buy" : 7434.050030250865, "sell" : 7434.050030250865, "symbol" : "CHF"},
 * "CLP" : {"15m" : 4725443.760146243, "last" : 4725443.760146243, "buy" : 4725443.760146243, "sell" : 4725443.760146243, "symbol" : "$"},
 * "CNY" : {"15m" : 48194.00829945235, "last" : 48194.00829945235, "buy" : 48194.00829945235, "sell" : 48194.00829945235, "symbol" : "¥"},
 * "DKK" : {"15m" : 48035.73399297992, "last" : 48035.73399297992, "buy" : 48035.73399297992, "sell" : 48035.73399297992, "symbol" : "kr"},
 * "EUR" : {"15m" : 6462.926330800001, "last" : 6462.926330800001, "buy" : 6462.926330800001, "sell" : 6462.926330800001, "symbol" : "€"},
 * "GBP" : {"15m" : 5651.711067989629, "last" : 5651.711067989629, "buy" : 5651.711067989629, "sell" : 5651.711067989629, "symbol" : "£"},
 * "HKD" : {"15m" : 58905.78993662382, "last" : 58905.78993662382, "buy" : 58905.78993662382, "sell" : 58905.78993662382, "symbol" : "$"},
 * "INR" : {"15m" : 506167.3807875865, "last" : 506167.3807875865, "buy" : 506167.3807875865, "sell" : 506167.3807875865, "symbol" : "₹"},
 * "ISK" : {"15m" : 793323.497056287, "last" : 793323.497056287, "buy" : 793323.497056287, "sell" : 793323.497056287, "symbol" : "kr"},
 * "JPY" : {"15m" : 821360.5333676001, "last" : 821360.5333676001, "buy" : 821360.5333676001, "sell" : 821360.5333676001, "symbol" : "¥"},
 * "KRW" : {"15m" : 8103769.116826082, "last" : 8103769.116826082, "buy" : 8103769.116826082, "sell" : 8103769.116826082, "symbol" : "₩"},
 * "NZD" : {"15m" : 10772.172421786989, "last" : 10772.172421786989, "buy" : 10772.172421786989, "sell" : 10772.172421786989, "symbol" : "$"},
 * "PLN" : {"15m" : 27810.301663041362, "last" : 27810.301663041362, "buy" : 27810.301663041362, "sell" : 27810.301663041362, "symbol" : "zł"},
 * "RUB" : {"15m" : 467570.619822708, "last" : 467570.619822708, "buy" : 467570.619822708, "sell" : 467570.619822708, "symbol" : "RUB"},
 * "SEK" : {"15m" : 66414.05967059285, "last" : 66414.05967059285, "buy" : 66414.05967059285, "sell" : 66414.05967059285, "symbol" : "kr"},
 * "SGD" : {"15m" : 10063.128754180441, "last" : 10063.128754180441, "buy" : 10063.128754180441, "sell" : 10063.128754180441, "symbol" : "$"},
 * "THB" : {"15m" : 240789.8601408753, "last" : 240789.8601408753, "buy" : 240789.8601408753, "sell" : 240789.8601408753, "symbol" : "฿"},
 * "TWD" : {"15m" : 225553.2741914504, "last" : 225553.2741914504, "buy" : 225553.2741914504, "sell" : 225553.2741914504, "symbol" : "NT$"}
 * }
 *
 * Created by jastrelax on 2018/4/7.
 */
@Entity(tableName = "exchange_rate", indices = @Index(value = "currency_code", unique = true))
public class ExchangeRate extends BaseEntity{

    @ColumnInfo(name = "currency_code")
    private String currencyCode;
    private Double delay;
    private Double last;
    private Double buy;
    private Double sell;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getDelay() {
        return delay;
    }

    public void setDelay(Double delay) {
        this.delay = delay;
    }

    public Double getLast() {
        return last;
    }

    public void setLast(Double last) {
        this.last = last;
    }

    public Double getBuy() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }

    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }
}

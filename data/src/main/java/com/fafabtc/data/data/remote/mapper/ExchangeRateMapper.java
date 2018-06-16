package com.fafabtc.data.data.remote.mapper;

import com.fafabtc.data.model.entity.ExchangeRate;
import com.fafabtc.base.data.remote.Mapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Map json object to exchange rate entity.
 * The raw json object is shown as below:
 * <pre>
 *   {
 *        "USD" : {"15m" : 6871.62, "last" : 6871.62, "buy" : 6871.62, "sell" : 6871.62, "symbol" : "$"},
 *        "AUD" : {"15m" : 8949.85, "last" : 8949.85, "buy" : 8949.85, "sell" : 8949.85, "symbol" : "$"},
 *        "BRL" : {"15m" : 23156.0, "last" : 23156.0, "buy" : 23156.0, "sell" : 23156.0, "symbol" : "R$"},
 *        "CAD" : {"15m" : 8788.37, "last" : 8788.37, "buy" : 8788.37, "sell" : 8788.37, "symbol" : "$"},
 *        "CHF" : {"15m" : 6587.06, "last" : 6587.06, "buy" : 6587.06, "sell" : 6587.06, "symbol" : "CHF"},
 *        "CLP" : {"15m" : 4155958.0, "last" : 4155958.0, "buy" : 4155958.0, "sell" : 4155958.0, "symbol" : "$"},
 *        "CNY" : {"15m" : 43304.29, "last" : 43304.29, "buy" : 43304.29, "sell" : 43304.29, "symbol" : "¥"},
 *        "DKK" : {"15m" : 41663.31, "last" : 41663.31, "buy" : 41663.31, "sell" : 41663.31, "symbol" : "kr"},
 *        "EUR" : {"15m" : 5581.31, "last" : 5581.31, "buy" : 5581.31, "sell" : 5581.31, "symbol" : "€"},
 *        "GBP" : {"15m" : 4877.96, "last" : 4877.96, "buy" : 4877.96, "sell" : 4877.96, "symbol" : "£"},
 *        "HKD" : {"15m" : 53935.03, "last" : 53935.03, "buy" : 53935.03, "sell" : 53935.03, "symbol" : "$"},
 *        "INR" : {"15m" : 446098.25, "last" : 446098.25, "buy" : 446098.25, "sell" : 446098.25, "symbol" : "₹"},
 *        "ISK" : {"15m" : 678698.63, "last" : 678698.63, "buy" : 678698.63, "sell" : 678698.63, "symbol" : "kr"},
 *        "JPY" : {"15m" : 732719.52, "last" : 732719.52, "buy" : 732719.52, "sell" : 732719.52, "symbol" : "¥"},
 *        "KRW" : {"15m" : 7355660.84, "last" : 7355660.84, "buy" : 7355660.84, "sell" : 7355660.84, "symbol" : "₩"},
 *        "NZD" : {"15m" : 9458.45, "last" : 9458.45, "buy" : 9458.45, "sell" : 9458.45, "symbol" : "$"},
 *        "PLN" : {"15m" : 23482.7, "last" : 23482.7, "buy" : 23482.7, "sell" : 23482.7, "symbol" : "zł"},
 *        "RUB" : {"15m" : 398478.59, "last" : 398478.59, "buy" : 398478.59, "sell" : 398478.59, "symbol" : "RUB"},
 *        "SEK" : {"15m" : 57661.17, "last" : 57661.17, "buy" : 57661.17, "sell" : 57661.17, "symbol" : "kr"},
 *        "SGD" : {"15m" : 9038.93, "last" : 9038.93, "buy" : 9038.93, "sell" : 9038.93, "symbol" : "$"},
 *        "THB" : {"15m" : 214616.27, "last" : 214616.27, "buy" : 214616.27, "sell" : 214616.27, "symbol" : "฿"},
 *        "TWD" : {"15m" : 201530.98, "last" : 201530.98, "buy" : 201530.98, "sell" : 201530.98, "symbol" : "NT$"}
 *    }
 * </pre>
 * Created by jastrelax on 2018/4/7.
 */
public enum ExchangeRateMapper implements Mapper<JsonObject, List<ExchangeRate>> {
    MAPPER;

    @Override
    public List<ExchangeRate> apply(JsonObject jsonObject) throws Exception {
        if (jsonObject == null)
            return null;
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (Map.Entry<String,JsonElement> entry : jsonObject.entrySet()) {
            JsonObject object = entry.getValue().getAsJsonObject();
            ExchangeRate rate = new ExchangeRate();
            rate.setCurrencyCode(entry.getKey());
            rate.setDelay(object.get("15m").getAsDouble());
            rate.setLast(object.get("last").getAsDouble());
            rate.setBuy(object.get("buy").getAsDouble());
            rate.setSell(object.get("sell").getAsDouble());
            exchangeRates.add(rate);
        }
        return exchangeRates;
    }
}

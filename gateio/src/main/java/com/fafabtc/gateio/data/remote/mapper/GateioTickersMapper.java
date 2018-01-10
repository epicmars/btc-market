package com.fafabtc.gateio.data.remote.mapper;

import com.fafabtc.common.json.GsonHelper;
import com.fafabtc.domain.data.remote.Mapper;
import com.fafabtc.gateio.data.remote.dto.GateioTickers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Set;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/7.
 */

public enum GateioTickersMapper implements Mapper<JsonObject, GateioTickers> {

    MAPPER;

    @Override
    public GateioTickers from(JsonObject source) {
        try {
            JsonObject result = new JsonObject();
            JsonArray array = new JsonArray();

            Set<String> pairNames = source.keySet();
            for (String name : pairNames) {
                JsonObject ticker = source.getAsJsonObject(name);
                ticker.addProperty("pair", name);
                array.add(ticker);
            }
            result.add("tickers", array);
            return GsonHelper.gson().fromJson(result, GateioTickers.class);
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }
}

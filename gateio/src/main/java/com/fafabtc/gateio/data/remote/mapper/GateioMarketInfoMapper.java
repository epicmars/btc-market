package com.fafabtc.gateio.data.remote.mapper;

import com.fafabtc.common.json.GsonHelper;
import com.fafabtc.domain.data.remote.Mapper;
import com.fafabtc.gateio.data.remote.dto.GateioMarketInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Set;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/7.
 */

public enum GateioMarketInfoMapper implements Mapper<JsonObject, GateioMarketInfo> {

    MAPPER;

    @Override
    public GateioMarketInfo from(JsonObject source) {
        try {
            JsonArray pairs = source.getAsJsonArray("pairs");
            for (JsonElement e : pairs) {
                JsonObject object = e.getAsJsonObject();
                Set<String> names = object.keySet();
                String pairName = names.iterator().next();
                JsonObject inner = object.getAsJsonObject(pairName);
                inner.addProperty("pair", pairName);
                pairs.remove(object);
                pairs.add(inner);
            }
            return GsonHelper.gson().fromJson(source, GateioMarketInfo.class);
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }
}

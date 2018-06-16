package com.fafabtc.gateio.data.remote.mapper;

import com.fafabtc.base.data.remote.Mapper;
import com.fafabtc.gateio.data.remote.dto.GateioPairs;

/**
 * Created by jastrelax on 2018/1/7.
 */

public enum GateioPairsMapper implements Mapper<String[], GateioPairs> {

    MAPPER;

    @Override
    public GateioPairs apply(String[] source) {
        GateioPairs pairs = new GateioPairs();
        pairs.setPairs(source);
        return pairs;
    }
}

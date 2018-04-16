package com.fafabtc.data.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.fafabtc.data.model.entity.exchange.Portfolio;

/**
 * Created by jastrelax on 2018/1/9.
 */

public class PortfolioStateConverter {

    @TypeConverter
    public String fromState(Portfolio.State state) {
        if (state == null) return null;
        return state.name();
    }

    @TypeConverter
    public Portfolio.State toState(String stateStr) {
        for (Portfolio.State s : Portfolio.State.values()) {
            if (s.name().equalsIgnoreCase(stateStr))
                return s;
        }
        return null;
    }
}

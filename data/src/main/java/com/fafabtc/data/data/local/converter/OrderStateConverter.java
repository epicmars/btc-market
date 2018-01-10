package com.fafabtc.data.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.fafabtc.data.model.entity.exchange.Order;

/**
 * Created by jastrelax on 2018/1/14.
 */

public class OrderStateConverter {

    @TypeConverter
    public String fromOrderState(Order.State state) {
        return state == null ? null : state.name();
    }

    @TypeConverter
    public Order.State fromString(String str) {
        for (Order.State s : Order.State.values()) {
            if (s.name().equalsIgnoreCase(str)) {
                return s;
            }
        }
        return null;
    }
}

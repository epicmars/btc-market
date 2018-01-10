package com.fafabtc.data.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.fafabtc.data.model.entity.exchange.Order;

/**
 * Created by jastrelax on 2018/1/14.
 */

public class OrderTypeConverter {

    @TypeConverter
    public String fromOrderType(Order.Type type) {
        return type == null ? null : type.name();
    }

    @TypeConverter
    public Order.Type fromTypeStr(String str) {
        for (Order.Type t : Order.Type.values()) {
            if (t.name().equalsIgnoreCase(str)) {
                return t;
            }
        }
        return null;
    }
}

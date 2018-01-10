package com.fafabtc.domain.data.local;

import android.arch.persistence.room.TypeConverter;

import com.fafabtc.common.utils.StringUtils;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class StringArrayConverter {

    @TypeConverter
    public String fromArray(String[] array) {
        if (array == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            if (StringUtils.isBlank(s))
                continue;
            sb.append(s).append(',');
        }
        int length = sb.length();
        if (length == 0)
            return null;
        return sb.deleteCharAt(length - 1).toString();
    }

    @TypeConverter
    public String[] toArray(String str) {
        if (str == null) return null;
        return str.split(",");
    }
}

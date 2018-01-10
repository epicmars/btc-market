package com.fafabtc.domain.data.local;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by jastrelax on 2018/1/7.
 */

public class DateConverter {
    @TypeConverter
    public Date fromTimestamp(Long timestamp) {
        return (timestamp == null)? null : new Date(timestamp);
    }

    @TypeConverter
    public Long fromDate(Date date) {
        return (date == null)? null : date.getTime();
    }
}

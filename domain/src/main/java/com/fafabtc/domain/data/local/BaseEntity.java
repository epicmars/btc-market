package com.fafabtc.domain.data.local;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by jastrelax on 2018/1/7.
 */

public class BaseEntity {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(index = true)
    public Date timestamp = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

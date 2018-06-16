package com.fafabtc.base.data.local;

import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by jastrelax on 2018/1/7.
 */

public class BaseEntity {

    @PrimaryKey(autoGenerate = true)
    public Long id;

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

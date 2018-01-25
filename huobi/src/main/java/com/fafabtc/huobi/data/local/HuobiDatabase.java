package com.fafabtc.huobi.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.fafabtc.domain.data.local.DateConverter;
import com.fafabtc.huobi.data.local.dao.HuobiPairDao;
import com.fafabtc.huobi.data.local.dao.HuobiTickerDao;
import com.fafabtc.huobi.domain.entity.HuobiPair;
import com.fafabtc.huobi.domain.entity.HuobiTicker;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Database(entities = {
        HuobiPair.class,
        HuobiTicker.class
}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class HuobiDatabase extends RoomDatabase {

    public abstract HuobiPairDao huobiPairDao();

    public abstract HuobiTickerDao huobiTickerDao();

}

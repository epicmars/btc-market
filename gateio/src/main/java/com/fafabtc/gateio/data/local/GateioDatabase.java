package com.fafabtc.gateio.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.fafabtc.domain.data.local.DateConverter;
import com.fafabtc.gateio.data.local.dao.GateioPairDao;
import com.fafabtc.gateio.data.local.dao.GateioTickerDao;
import com.fafabtc.gateio.model.entity.GateioPair;
import com.fafabtc.gateio.model.entity.GateioTicker;
import com.fafabtc.gateio.model.entity.MarketInfo;
import com.fafabtc.gateio.model.entity.MarketList;

/**
 * Created by jastrelax on 2018/1/7.
 */

@Database(entities = {GateioPair.class, MarketInfo.class, MarketList.class, GateioTicker.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class GateioDatabase extends RoomDatabase{

    public abstract GateioPairDao gateioPairDao();

    public abstract GateioTickerDao gateioTickerDao();

}

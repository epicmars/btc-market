package com.fafabtc.huobi.data.local;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by jastrelax on 2018/1/25.
 */

public class HuobiDatabaseHelper {

    private static volatile HuobiDatabase huobiDatabase;

    public static HuobiDatabase getHuobiDatabase(Context context) {
        if (null == huobiDatabase) {
            synchronized (HuobiDatabaseHelper.class) {
                huobiDatabase = Room.databaseBuilder(context, HuobiDatabase.class, "huobi.db")
                        .build();
            }
        }
        return huobiDatabase;
    }
}

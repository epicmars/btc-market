package com.fafabtc.gateio.data.local;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by jastrelax on 2018/1/7.
 */

public class GateioDataBaseHelper {

    private static volatile GateioDatabase gateioDatabase;

    public static GateioDatabase getDatabase(Context context) {
        if (null == gateioDatabase) {
            synchronized (GateioDataBaseHelper.class) {
                if (null == gateioDatabase) {
                    gateioDatabase = Room.databaseBuilder(context.getApplicationContext(), GateioDatabase.class, "gateio.db")
                            .build();
                }
            }
        }
        return gateioDatabase;
    }
}

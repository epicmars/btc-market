package com.fafabtc.binance.data.local;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by jastrelax on 2018/1/13.
 */

public class BinanceDatabaseHelper {

    private static volatile BinanceDatabase binanceDatabase;

    public static BinanceDatabase binanceDatabase(Context context) {
        if (binanceDatabase == null) {
            synchronized (BinanceDatabaseHelper.class) {
                if (binanceDatabase == null) {
                    binanceDatabase = Room.databaseBuilder(context,
                            BinanceDatabase.class,
                            "binance.db")
                            .build();
                }
            }
        }
        return binanceDatabase;
    }

}

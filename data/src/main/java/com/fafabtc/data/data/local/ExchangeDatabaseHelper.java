package com.fafabtc.data.data.local;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class ExchangeDatabaseHelper {

    private static volatile ExchangeDatabase exchangeDatabase;

    public static ExchangeDatabase exchangeDatabase(Context context) {
        if (exchangeDatabase == null) {
            synchronized (ExchangeDatabaseHelper.class) {
                if (exchangeDatabase == null) {
                    exchangeDatabase = Room.databaseBuilder(context.getApplicationContext(),
                            ExchangeDatabase.class, "exchange.db")
                            .build();
                }
            }
        }
        return exchangeDatabase;
    }
}

package com.fafabtc.data.data.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

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

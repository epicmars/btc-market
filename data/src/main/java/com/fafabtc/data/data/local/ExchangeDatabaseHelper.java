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

    private static volatile ExchangeDatabaseHelper instance;

    private ExchangeDatabase exchangeDatabase;

    public ExchangeDatabaseHelper(Context context) {
        exchangeDatabase = Room.databaseBuilder(context.getApplicationContext(),
                ExchangeDatabase.class, "exchange.db")
                .build();
    }

    public static ExchangeDatabaseHelper instance(Context context) {
        if (instance == null) {
            synchronized (ExchangeDatabaseHelper.class) {
                if (instance == null) {
                    instance = new ExchangeDatabaseHelper(context);
                }
            }
        }

        return instance;
    }

    public ExchangeDatabase exchangeDatabase() {
        return exchangeDatabase;
    }
}

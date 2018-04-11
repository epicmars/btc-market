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
                            .addMigrations(new Migration_1_2(1, 2))
                            .build();
                }
            }
        }
        return exchangeDatabase;
    }

    public static class Migration_1_2 extends Migration {

        public Migration_1_2(int startVersion, int endVersion) {
            super(startVersion, endVersion);
        }

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `exchange_rate` (`currency_code` TEXT, `delay` REAL, `last` REAL, `buy` REAL, `sell` REAL)");
            database.execSQL("CREATE UNIQUE INDEX `index_exchange_rate_currency_code` ON `exchange_rate` (`currency_code`)");
        }
    }
}

package com.fafabtc.data.global;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.fafabtc.common.utils.DateTimeUtils;
import com.fafabtc.data.provider.Providers;

import java.util.Date;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/19.
 */
@Singleton
public class AssetsStateRepository {

    Context context;

    private ContentResolver contentResolver;

    private Uri ASSETS_STATE_URI = Uri.parse(Providers.AssetsState.URI);

    @Inject
    public AssetsStateRepository(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    public Single<Boolean> isAssetsInitialized() {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Cursor cursor = contentResolver.query(ASSETS_STATE_URI, null, null, null, null);
                Boolean isAssetsInitialized = false;
                if (cursor != null) {
                    cursor.moveToFirst();
                    int index = cursor.getColumnIndex(Providers.AssetsState.IS_ASSETS_INITIALIZED);
                    int assetsInitialized = cursor.getInt(index);
                    isAssetsInitialized = assetsInitialized > 0;
                    cursor.close();
                }
                return isAssetsInitialized;
            }
        });
    }

    public Completable assetsInitialized() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                ContentValues values = new ContentValues();
                values.put(Providers.AssetsState.IS_ASSETS_INITIALIZED, true);
                contentResolver.update(ASSETS_STATE_URI, values, null, null);
            }
        });
    }

    public Single<Date> getUpdateTime() {
        return Single.fromCallable(new Callable<Date>() {
            @Override
            public Date call() throws Exception {
                Cursor cursor = contentResolver.query(ASSETS_STATE_URI, null, null, null, null);
                long updateTime = 0;
                if (cursor != null) {
                    cursor.moveToFirst();
                    int index = cursor.getColumnIndex(Providers.AssetsState.UPDATE_TIME);
                    updateTime = cursor.getLong(index);
                    cursor.close();
                }
                return new Date(updateTime);
            }
        });
    }

    public Completable setUpdateTime(final Date date) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                ContentValues values = new ContentValues();
                values.put(Providers.AssetsState.UPDATE_TIME, date.getTime());
                contentResolver.update(ASSETS_STATE_URI, values, null, null);
            }
        });
    }

    public Single<String> getFormatedUpdateTime() {
        return getUpdateTime().map(new Function<Date, String>() {
            @Override
            public String apply(Date date) throws Exception {
                long updateTime = date.getTime();
                String formatDate = "";
                if (updateTime > 0) {
                    formatDate = DateTimeUtils.formatStandard(new Date(updateTime));
                }
                return formatDate;
            }
        });
    }
}

package com.fafabtc.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fafabtc.data.data.local.ExchangeDatabase;
import com.fafabtc.data.data.local.ExchangeDatabaseHelper;

/**
 * Created by jastrelax on 2018/5/20.
 */
public class ExchangeDatabaseProvider extends ContentProvider{

    private ExchangeDatabase exchangeDatabase;

    @Override
    public boolean onCreate() {
        exchangeDatabase = ExchangeDatabaseHelper.instance(getContext()).exchangeDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String table = uri.getLastPathSegment();
        String query = SQLiteQueryBuilder.buildQueryString(false, table, projection, selection, null, null, sortOrder, null);
        return exchangeDatabase.query(query, selectionArgs);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String table = uri.getLastPathSegment();
        long id = exchangeDatabase.getOpenHelper().getWritableDatabase().insert(table, SQLiteDatabase.CONFLICT_REPLACE, values);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table = uri.getLastPathSegment();
        return exchangeDatabase.getOpenHelper().getWritableDatabase().delete(table, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table = uri.getLastPathSegment();
        return exchangeDatabase.getOpenHelper().getWritableDatabase().update(table, SQLiteDatabase.CONFLICT_REPLACE, values, selection, selectionArgs);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}

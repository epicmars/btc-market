package com.fafabtc.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fafabtc.common.file.SharedPreferenceUtils;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/4/7.
 */
public class SharedPreferenceProvider extends ContentProvider {

    public static final String DATA_FIELD_NAME = "data";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String prefName = uri.getLastPathSegment();
        if (prefName == null) {
            Timber.e("SharedPreference name is not specified!");
            return null;
        }
        MatrixCursor cursor = new MatrixCursor(new String[]{prefName});
        String data = SharedPreferenceUtils.getString(getContext(), prefName, DATA_FIELD_NAME);
        cursor.addRow(new String[]{data});
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String prefName = uri.getLastPathSegment();
        if (prefName == null) {
            Timber.e("SharedPreference name is not specified!");
            return null;
        }
        if (values == null) return null;
        SharedPreferenceUtils.updateField(getContext(), prefName, DATA_FIELD_NAME, values.get(DATA_FIELD_NAME));
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String prefName = uri.getLastPathSegment();
        if (prefName == null) {
            Timber.e("SharedPreference name is not specified!");
            return 0;
        }
        SharedPreferenceUtils.updateField(getContext(), prefName, DATA_FIELD_NAME, "");
        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (values == null) {
            Timber.e("No values are provided!");
            return 0;
        }
        String prefName = uri.getLastPathSegment();
        SharedPreferenceUtils.updateField(getContext(), prefName, DATA_FIELD_NAME, values.get(DATA_FIELD_NAME));
        return 1;
    }
}

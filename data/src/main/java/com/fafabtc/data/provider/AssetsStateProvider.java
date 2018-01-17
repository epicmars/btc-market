package com.fafabtc.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fafabtc.data.global.AssetsState;

/**
 * Created by jastrelax on 2018/1/22.
 */

public class AssetsStateProvider extends ContentProvider {

    @Override
    public boolean onCreate() {

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Long updateTime = AssetsState.getUpdateTime(getContext());
        Integer isAssetsInitialized = AssetsState.isAssetsInitialized(getContext());
        MatrixCursor cursor = new MatrixCursor(new String[]{Providers.AssetsState.UPDATE_TIME, Providers.AssetsState.IS_ASSETS_INITIALIZED});
        cursor.addRow(new Object[]{updateTime, isAssetsInitialized});
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
        if (values == null) return null;
        Long updateTime = values.getAsLong(Providers.AssetsState.UPDATE_TIME);
        Boolean isAssetsInitialized = values.getAsBoolean(Providers.AssetsState.IS_ASSETS_INITIALIZED);
        if (updateTime != null) {
            AssetsState.setUpdateTime(getContext(), updateTime);
        }
        if (isAssetsInitialized != null) {
            AssetsState.setAssetsInitialized(getContext(), isAssetsInitialized);
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (values == null) return 0;
        Long updateTime = values.getAsLong(Providers.AssetsState.UPDATE_TIME);
        Boolean isAssetsInitialized = values.getAsBoolean(Providers.AssetsState.IS_ASSETS_INITIALIZED);
        if (updateTime != null) {
            AssetsState.setUpdateTime(getContext(), updateTime);
        }
        if (isAssetsInitialized != null) {
            AssetsState.setAssetsInitialized(getContext(), isAssetsInitialized);
        }
        return 1;
    }
}

package com.fafabtc.data.data.global;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.fafabtc.common.json.GsonHelper;
import com.fafabtc.data.provider.Providers;
import com.fafabtc.data.provider.SharedPreferenceDataProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jastrelax on 2018/4/7.
 */
@Singleton
public class SharedPreferenceDataHelper {

    private ContentResolver resolver;

    @Inject
    public SharedPreferenceDataHelper(Context context) {
        this.resolver = context.getContentResolver();
    }

    public <T> T[] find(Class<T> clazz, Class<T[]> arrayClazz) {
        if (clazz == null)
            return null;
        String prefName = clazz.getSimpleName();
        Uri uri = Uri.parse(Providers.XML_URL + prefName);
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor == null)
            return null;
        try {
            if (cursor.moveToFirst()) {

                int index = cursor.getColumnIndex(prefName);
                String data = cursor.getString(index);
                return GsonHelper.gson().fromJson(data, arrayClazz);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public <T> void save(Class<T> clazz, T[] obj) {
        if (obj == null || obj.length == 0) return;
        Uri uri = Uri.parse(Providers.XML_URL + clazz.getSimpleName());
        ContentValues values = new ContentValues();
        values.put(SharedPreferenceDataProvider.DATA_FIELD_NAME, GsonHelper.gson().toJson(obj));
        resolver.update(uri, values, null, null);
    }
}

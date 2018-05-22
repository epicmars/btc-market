package com.fafabtc.data.data.global;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.fafabtc.common.json.GsonHelper;
import com.fafabtc.data.provider.Providers;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jastrelax on 2018/5/20.
 */
@Singleton
public class ExchangeDatabaseProviderHelper {

    private ContentResolver contentResolver;

    @Inject
    public ExchangeDatabaseProviderHelper(Context context) {
        contentResolver = context.getApplicationContext().getContentResolver();
    }

    public <T> T[] find(Class<T[]> clazz, String selection, String[] selectionArgs) {
        Uri uri = Uri.parse(Providers.DB_URL);
        uri = Uri.withAppendedPath(uri, clazz.getComponentType().getSimpleName());
        Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    JsonArray array = new JsonArray();
                    do {
                        String[] columns = cursor.getColumnNames();
                        JsonObject jsonObject = new JsonObject();
                        for (String column : columns) {
                            int index = cursor.getColumnIndex(column);
                            int type = cursor.getType(index);
                            switch (type) {
                                case Cursor.FIELD_TYPE_FLOAT:
                                    jsonObject.addProperty(column, cursor.getDouble(index));
                                    break;
                                case Cursor.FIELD_TYPE_INTEGER:
                                    jsonObject.addProperty(column, cursor.getLong(index));
                                    break;
                                case Cursor.FIELD_TYPE_STRING:
                                    jsonObject.addProperty(column, cursor.getString(index));
                                    break;
                            }
                        }
                        array.add(jsonObject);
                    } while (cursor.moveToNext());
                    return GsonHelper.instance().getGson().fromJson(array, clazz);
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public <T> void insert(Object object) {
        Uri uri = Uri.parse(Providers.DB_URL);
        uri = Uri.withAppendedPath(uri, object.getClass().getSimpleName());
        contentResolver.insert(uri, fromPojo(object));
    }

    public <T> void update(Object object, String selection, String[] selectionArgs) {
        Uri uri = Uri.parse(Providers.DB_URL);
        uri = Uri.withAppendedPath(uri, object.getClass().getSimpleName());
        contentResolver.update(uri, fromPojo(object), selection, selectionArgs);
    }

    private <T> ContentValues fromPojo(Object obj) {
        if (obj == null)
            return null;
        JsonObject jsonElement = GsonHelper.instance().getGson().toJsonTree(obj, obj.getClass()).getAsJsonObject();
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, JsonElement> entry : jsonElement.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value.isJsonPrimitive()) {
                JsonPrimitive primitive = (JsonPrimitive) value;
                if (primitive.isBoolean()) {
                    contentValues.put(key, primitive.getAsBoolean());
                } else if (primitive.isNumber()) {
                    Number number = primitive.getAsNumber();
                    if (number instanceof Float) {
                        contentValues.put(key, number.floatValue());
                    } else if (number instanceof Double) {
                        contentValues.put(key, number.doubleValue());
                    } else if (number instanceof Integer) {
                        contentValues.put(key, number.intValue());
                    } else if (number instanceof Long) {
                        contentValues.put(key, number.longValue());
                    } else if (number instanceof Short) {
                        contentValues.put(key, number.shortValue());
                    }
                } else if (primitive.isString()) {
                    contentValues.put(key, primitive.getAsString());
                }
            }
        }
        return contentValues;
    }
}

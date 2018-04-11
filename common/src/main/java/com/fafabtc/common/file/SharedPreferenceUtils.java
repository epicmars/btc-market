package com.fafabtc.common.file;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class SharedPreferenceUtils {

    public static <T> void updateField(Context context, String preference, String key, T value) {
        if (context == null || preference == null || key == null || value == null)
            return;
        SharedPreferences pref= context.getSharedPreferences(preference, 0);
        SharedPreferences.Editor editor = pref.edit();
        if (String.class.isInstance(value))
        {
            editor.putString(key, (String) value);
        } else if (int.class.isInstance(value) || Integer.class.isInstance(value)) {
            editor.putInt(key, (Integer) value);
        } else if (long.class.isInstance(value) || Long.class.isInstance(value)) {
            editor.putLong(key, (Long) value);
        } else if (float.class.isInstance(value) || Float.class.isInstance(value)) {
            editor.putFloat(key, (Float) value);
        } else if (boolean.class.isInstance(value) || Boolean.class.isInstance(value)) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.apply();
    }

    public static void updateField(Context context, String preference, String key, Set<String> value) {
        if (context == null || preference == null || key == null || value == null)
            return;
        SharedPreferences pref= context.getSharedPreferences(preference, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static String getString(Context context, String preference, String key) {
        if (context == null || preference == null || key == null)
            return null;
        SharedPreferences pref = context.getSharedPreferences(preference, 0);
        return pref.getString(key, "");
    }

    public static SharedPreferences getPreference(Context context, String preference) {
        if (context == null || preference == null)
            return null;
        SharedPreferences pref= context.getSharedPreferences(preference, 0);
        return pref;
    }
}

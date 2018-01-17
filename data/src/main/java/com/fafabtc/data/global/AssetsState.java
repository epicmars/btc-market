package com.fafabtc.data.global;

import android.content.Context;

import com.fafabtc.common.file.SharedPreferenceUtils;

/**
 * Created by jastrelax on 2018/1/18.
 */

public class AssetsState {

    public static final String ASSETS_DATA_FILE = "assets.json";

    public static final String ASSETS_PREFERENCE = "assets_pref";

    public static final String KEY_IS_INITIALIZED = "isAssetsInitialized";
    public static final String KEY_UPDATE_TIME = "updateTime";

    public static int isAssetsInitialized(Context context) {
        return SharedPreferenceUtils.getPreference(context, ASSETS_PREFERENCE)
                .getInt(KEY_IS_INITIALIZED, 0);
    }

    public static void setAssetsInitialized(Context context, boolean initialized) {
        SharedPreferenceUtils.updateField(context, AssetsState.ASSETS_PREFERENCE, AssetsState.KEY_IS_INITIALIZED, initialized ? 1 : 0);
    }

    public static long getUpdateTime(Context context) {
        return SharedPreferenceUtils.getPreference(context, ASSETS_PREFERENCE)
                .getLong(KEY_UPDATE_TIME, 0);
    }

    public static void setUpdateTime(Context context, long date) {
        SharedPreferenceUtils.updateField(context, AssetsState.ASSETS_PREFERENCE, AssetsState.KEY_UPDATE_TIME, date);
    }
}

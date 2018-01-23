package com.fafabtc.app.settings;

import android.content.Context;
import android.preference.PreferenceManager;

import com.fafabtc.app.R;

import static com.fafabtc.common.utils.DateTimeUtils.ONE_MINUTE;

/**
 * Created by jastrelax on 2018/1/23.
 */

public class Settings {

    public static final long INTERVAL_MINUTES = 30;

    public static long getTickerUpdateIntervalMillis(Context context) {
        return Long.valueOf(getTickerUpdateIntervalMinutes(context)) * ONE_MINUTE;
    }

    public static String getTickerUpdateIntervalMinutes(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_key_app_widget_update_frequency), String.valueOf(INTERVAL_MINUTES));
    }
}

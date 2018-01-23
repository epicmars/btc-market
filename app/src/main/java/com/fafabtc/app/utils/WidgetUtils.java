package com.fafabtc.app.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import com.fafabtc.app.receiver.AssetsWidgetProvider;

/**
 * Created by jastrelax on 2018/1/23.
 */

public class WidgetUtils {

    public static boolean isWidgetAvailable(Context context) {
        return getWidgetIds(context).length > 0;
    }

    public static int[] getWidgetIds(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        return manager.getAppWidgetIds(new ComponentName(context, AssetsWidgetProvider.class));
    }
}

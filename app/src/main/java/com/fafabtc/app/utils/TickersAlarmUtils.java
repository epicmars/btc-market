package com.fafabtc.app.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fafabtc.app.constants.Services;
import com.fafabtc.app.service.WidgetService;
import com.fafabtc.app.settings.Settings;


/**
 * Created by jastrelax on 2018/1/23.
 */

public class TickersAlarmUtils {

    public static void scheduleUpdate(Context context) {
        scheduleUpdate(context, Settings.getTickerUpdateIntervalMillis(context));
    }

    public static void scheduleUpdate(Context context, long interval) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(context);
        am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, pendingIntent);
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(Services.Actions.ACTION_UPDATE_TICKERS);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public static void cancelUpdate(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(getPendingIntent(context));
    }

    public static void frequencyChanged(Context context, long newFrequency) {
        cancelUpdate(context);
        scheduleUpdate(context, newFrequency);
    }
}

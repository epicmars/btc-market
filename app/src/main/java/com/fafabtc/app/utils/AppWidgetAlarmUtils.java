package com.fafabtc.app.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fafabtc.app.constants.Services;
import com.fafabtc.app.service.WidgetService;

/**
 * Created by jastrelax on 2018/1/23.
 */

public class AppWidgetAlarmUtils {

    public static final long INTERVAL_MILLIS = 60 * 1000;

    public static void scheduleUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getAlarmInent(context);
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), INTERVAL_MILLIS, pendingIntent);
    }

    public static void clearUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getAlarmInent(context));
    }

    private static PendingIntent getAlarmInent(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(Services.Actions.ACTION_UPDATE_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        return pendingIntent;
    }
}

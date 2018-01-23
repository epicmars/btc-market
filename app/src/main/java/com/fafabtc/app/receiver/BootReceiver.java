package com.fafabtc.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fafabtc.app.utils.AppWidgetAlarmUtils;
import com.fafabtc.app.utils.TickersAlarmUtils;
import com.fafabtc.app.utils.WidgetUtils;

/**
 * Created by jastrelax on 2018/1/23.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleWidgetUpdate(context);
    }

    private void scheduleWidgetUpdate(Context context) {
        if (WidgetUtils.isWidgetAvailable(context)) {
            AppWidgetAlarmUtils.scheduleUpdate(context);
            TickersAlarmUtils.scheduleUpdate(context);
        }
    }
}

package com.fafabtc.app.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.app.constants.Services;
import com.fafabtc.app.service.WidgetService;
import com.fafabtc.app.utils.AppWidgetAlarmUtils;
import com.fafabtc.app.utils.TickersAlarmUtils;
import com.fafabtc.common.utils.NumberUtils;
import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.model.vo.WidgetData;

public class AssetsWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_WIDGET_DATA = "AssetsWidgetProvider.EXTRA_WIDGET_DATA";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (Broadcasts.WidgetActions.ACTION_UPDATE_WIDGET.equals(action) ||
                DataBroadcasts.Actions.ACTION_DATA_INITIALIZED.equals(action) ||
                DataBroadcasts.Actions.ACTION_TICKER_UPDATED.equals(action) ||
                Broadcasts.Actions.ACTION_CURRENT_ASSETS_CHANGED.equals(action)) {
            WidgetData data = intent.getParcelableExtra(EXTRA_WIDGET_DATA);
            onUpdate(context, data);
            return;
        } else if (Broadcasts.WidgetActions.ACTION_MANUL_UPDATE_TICKERS.equals(action)) {
            showLoadingProgress(context);
        } else {
            onUpdate(context, null);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AppWidgetAlarmUtils.scheduleUpdate(context);
        TickersAlarmUtils.scheduleUpdate(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        AppWidgetAlarmUtils.clearUpdate(context);
        TickersAlarmUtils.cancelUpdate(context);
    }

    public void onUpdate(final Context context, WidgetData data) {
        if (data == null) {
            Intent intent = new Intent(context, WidgetService.class);
            intent.setAction(Services.Actions.ACTION_UPDATE_WIDGET);
            context.startService(intent);
        } else {
            refreshWidget(context, data);
        }
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void showLoadingProgress(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, AssetsWidgetProvider.class));
        int n = appWidgetIds.length;
        for (int i = 0; i < n; i++) {
            final int appWidgetId = appWidgetIds[i];
            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_account_assets);
            rv.setViewVisibility(R.id.pb_loading, View.VISIBLE);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }

    private void hideLoadingProgress(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, AssetsWidgetProvider.class));
        int n = appWidgetIds.length;
        for (int i = 0; i < n; i++) {
            final int appWidgetId = appWidgetIds[i];
            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_account_assets);
            rv.setViewVisibility(R.id.pb_loading, View.VISIBLE);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }

    private void refreshWidget(final Context context, WidgetData data) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, AssetsWidgetProvider.class));
        int n = appWidgetIds.length;
        for (int i = 0; i < n; i++) {
            final int appWidgetId = appWidgetIds[i];
            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_account_assets);
            rv.setOnClickPendingIntent(R.id.btn_refresh, manulUpdateTickers(context));
            rv.setTextViewText(R.id.tv_assets_name, data.getAccountAssets().getName());
            rv.setTextViewText(R.id.tv_update_time, context.getString(R.string.update_time_format, data.getUpdateTime()));
            rv.setTextViewText(R.id.tv_total, NumberUtils.formatBalance(data.getVolume()));
            rv.setViewVisibility(R.id.pb_loading, View.INVISIBLE);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }

    public static void update(Context context, WidgetData data) {
        Intent intent = new Intent(context, AssetsWidgetProvider.class);
        intent.setAction(Broadcasts.WidgetActions.ACTION_UPDATE_WIDGET);
        intent.putExtra(EXTRA_WIDGET_DATA, data);
        context.sendBroadcast(intent);
    }

    public static PendingIntent manulUpdateTickers(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(Services.Actions.ACTION_MANUL_UPDATE_TICKERS);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        return pendingIntent;
    }

}

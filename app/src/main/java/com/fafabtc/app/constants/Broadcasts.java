package com.fafabtc.app.constants;

/**
 * Created by jastrelax on 2018/1/11.
 */

public interface Broadcasts {

    interface Actions {
        String ACTION_ORDER_CREATED = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_ORDER_CREATED";
        String ACTION_ORDER_DEAL = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_ORDER_DEAL";
        String ACTION_BALANCE_DEPOSITED = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_BALANCE_DEPOSITED";
        String ACTION_ASSETS_CREATED = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_ASSETS_CREATED";
        String ACTION_CURRENT_ASSETS_CHANGED = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_CURRENT_ASSETS_CHANGED";
    }

    interface WidgetActions {
        String ACTION_UPDATE_WIDGET = "com.fafabtc.app.constants.Boradcast.WidgetActions.ACTION_UPDATE_WIDGET";
        String ACTION_MANUL_UPDATE_TICKERS = "com.fafabtc.app.constants.Boradcast.WidgetActions.ACTION_MANUL_UPDATE_TICKERS";
    }
}

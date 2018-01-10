package com.fafabtc.app.constants;

/**
 * Created by jastrelax on 2018/1/11.
 */

public interface Broadcast {

    interface Actions {
        String ACTION_DATA_INITIALIZED = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_DATA_INITIALIZED";
        String ACTION_TICKER_UPDATED = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_TICKER_UPDATED";
        String ACTION_ORDER_CREATED = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_ORDER_CREATED";
        String ACTION_ORDER_DEAL = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_ORDER_DEAL";
        String ACTION_BALANCE_DEPOSITED = "com.fafabtc.app.constants.Boradcast.Actions.ACTION_BALANCE_DEPOSITED";
    }
}

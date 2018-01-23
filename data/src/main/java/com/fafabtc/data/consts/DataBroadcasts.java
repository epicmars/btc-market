package com.fafabtc.data.consts;

/**
 * Created by jastrelax on 2018/1/22.
 */

public interface DataBroadcasts {

    interface Actions {
        // initiate
        String ACTION_INITIATE_EXCHANGE = "com.fafabtc.data.consts.DataBroadcasts.Actions.ACTION_INITIATE_EXCHANGE";
        String ACTION_INITIATE_ASSETS = "com.fafabtc.data.consts.DataBroadcasts.Actions.ACTION_INITIATE_ASSETS";
        String ACTION_FETCH_TICKERS = "com.fafabtc.data.consts.DataBroadcasts.Actions.ACTION_FETCH_TICKERS";
        String ACTION_DATA_INITIALIZED = "com.fafabtc.data.consts.DataBroadcasts.Actions.ACTION_DATA_INITIALIZED";
        // update
        String ACTION_TICKER_UPDATED = "com.fafabtc.data.consts.DataBroadcasts.Actions.ACTION_TICKER_UPDATED";
    }
}

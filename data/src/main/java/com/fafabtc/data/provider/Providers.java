package com.fafabtc.data.provider;

/**
 * Created by jastrelax on 2018/1/22.
 */

public interface Providers {

    interface AssetsState {
        String URI = "content://com.fafabtc.data.provider/assets_state";
        // columns
        String UPDATE_TIME = "updateTime";
        String IS_ASSETS_INITIALIZED = "isAssetsInitialized";
    }
}

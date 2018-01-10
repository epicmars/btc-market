package com.fafabtc.common.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by jastrelax on 2018/1/7.
 */


public class GsonHelper {

    private static volatile Gson instance;
    private static volatile Gson pretty;

    public static Gson gson() {
        if (null == instance) {
            synchronized (GsonHelper.class) {
                if (null == instance) {
                    instance = new GsonBuilder().create();
                }
            }
        }
        return instance;
    }

    public static Gson prettyGson() {
        if (null == pretty) {
            synchronized (GsonHelper.class) {
                if (null == pretty) {
                    pretty = new GsonBuilder()
                            .setPrettyPrinting()
                            .create();
                }
            }
        }
        return pretty;
    }
}

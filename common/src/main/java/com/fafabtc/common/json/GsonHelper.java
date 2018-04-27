package com.fafabtc.common.json;

import com.fafabtc.common.utils.DateTimeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by jastrelax on 2018/1/7.
 */


public class GsonHelper {

    private static volatile GsonHelper instance;
    private Gson gson;
    private Gson pretty;
    private GsonBuilder builder;

    public GsonHelper() {
        gson = new GsonBuilder()
                .setDateFormat(DateTimeUtils.STANDARD)
                .create();

        pretty = new GsonBuilder()
                .setDateFormat(DateTimeUtils.STANDARD)
                .setPrettyPrinting()
                .create();
    }

    public static GsonHelper instance() {
        if (null == instance) {
            synchronized (GsonHelper.class) {
                if (null == instance) {
                    instance = new GsonHelper();
                }
            }
        }
        return instance;
    }

    public static Gson gson() {
        return instance().getGson();
    }

    public static Gson prettyGson() {
        return instance().getPrettyGson();
    }

    public Gson getGson() {
        return gson;
    }

    public Gson getPrettyGson() {
        return pretty;
    }
}

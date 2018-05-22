package com.fafabtc.common.json;

import com.fafabtc.common.utils.DateTimeUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
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
        GsonBuilder builder = new GsonBuilder()
//                .registerTypeAdapter(Boolean.class, BooleanTypeAdapter.class)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat(DateTimeUtils.STANDARD);
        gson = builder
                .create();

        pretty = builder
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

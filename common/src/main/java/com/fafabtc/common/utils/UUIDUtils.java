package com.fafabtc.common.utils;

import java.util.UUID;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class UUIDUtils {

    public static String newUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}

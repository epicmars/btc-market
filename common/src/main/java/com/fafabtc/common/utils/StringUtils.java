package com.fafabtc.common.utils;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class StringUtils {

    public static boolean isBlank(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        final int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!isWhitespace(str.codePointAt(i)))
                return false;
        }
        return true;
    }

    public static boolean isWhitespace(int c){
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
    }

    public static boolean isNumber(String str) {
        if (isBlank(str)) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

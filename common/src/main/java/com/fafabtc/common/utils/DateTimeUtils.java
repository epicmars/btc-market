package com.fafabtc.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jastrelax on 2018/1/11.
 */

public class DateTimeUtils {

    public static final long ONE_MINUTE = 60 * 1000;

    public static final String STANDARD = "yyyy-MM-dd HH:mm:ss.SSSZ";

    public static final DateFormat FORMAT_STANDARD = new SimpleDateFormat(STANDARD, Locale.CHINESE);

    public static String formatStandard(Date date) {
        return FORMAT_STANDARD.format(date);
    }

}

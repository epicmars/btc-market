package com.fafabtc.analysis.bugly;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class BuglyHelper {

    public static void init(Context context) {
        CrashReport.initCrashReport(context);


    }

}

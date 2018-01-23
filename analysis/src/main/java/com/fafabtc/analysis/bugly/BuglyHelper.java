package com.fafabtc.analysis.bugly;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.fafabtc.analysis.BuildConfig;
import com.tencent.bugly.Bugly;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class BuglyHelper {

    private static final String TAG = BuglyHelper.class.getName();

    public static void init(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String appId = info.applicationInfo.metaData.getString("BUGLY_APPID");
            Bugly.init(context.getApplicationContext(), appId, BuildConfig.DEBUG);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

}

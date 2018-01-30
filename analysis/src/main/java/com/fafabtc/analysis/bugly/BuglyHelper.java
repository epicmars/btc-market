package com.fafabtc.analysis.bugly;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.fafabtc.analysis.BuildConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class BuglyHelper {

    private static final String TAG = BuglyHelper.class.getName();

    public static void init(Context context, @DrawableRes int largeIconRes) {
        try {
            // config
            Beta.initDelay = 1000;
            Beta.autoCheckUpgrade = false;
            Beta.largeIconId = largeIconRes;
            Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // initiate
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String appId = info.applicationInfo.metaData.getString("BUGLY_APPID");
            Bugly.init(context.getApplicationContext(), appId, BuildConfig.DEBUG);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void checkUpgrade() {
        Beta.checkUpgrade();
    }

}

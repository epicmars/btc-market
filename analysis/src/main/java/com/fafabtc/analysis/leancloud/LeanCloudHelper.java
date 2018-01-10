package com.fafabtc.analysis.leancloud;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.fafabtc.analysis.BuildConfig;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class LeanCloudHelper {

    public static final String TAG = LeanCloudHelper.class.getName();


    public static final void init(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String appid = info.applicationInfo.metaData.getString("LEANCLOUD_APPID");
            String appkey = info.applicationInfo.metaData.getString("LEANCLOUD_APPKEY");
            AVOSCloud.initialize(context, appid, appkey);
            // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
            AVOSCloud.setDebugLogEnabled(BuildConfig.DEBUG);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void testLeancloud() {
        // 测试 SDK 是否正常工作的代码
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words","Hello World!");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Log.d("saved","success!");
                }
            }
        });
    }
}

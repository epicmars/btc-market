package com.fafabtc.app.utils;

import android.content.Context;
import android.content.res.Resources;

import com.fafabtc.app.R;

/**
 * Created by jastrelax on 2018/1/15.
 */

public class UiUtils {

    public static int getStatusBarHeight(Context context) {
        final int statusBarHeight;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        } else {
            statusBarHeight = resources.getDimensionPixelSize(R.dimen.app_status_bar_height);
        }
        return statusBarHeight;
    }
}

package com.fafabtc.app.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fafabtc.app.service.MainService;
import com.fafabtc.app.service.SyncService;
import com.fafabtc.app.service.TradeService;
import com.fafabtc.app.ui.MainActivity;

/**
 * Splash creeen.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int DISMISS_DELAY_SHORT = 1000;
    private static final int DISMISS_DELAY = 3000;
    private static final int DISMISS_DELAY_LONG = 6000;

    private View mContentView;

    private Handler mHandler = new Handler();

    private boolean dismissed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
//        mContentView = findViewById(R.id.fullscreen_content);
//
//        // Hide UI.
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//
//        // Note that some of these constants are new as of API 16 (Jelly Bean)
//        // and API 19 (KitKat). It is safe to use them, as they are inlined
//        // at compile-time and do nothing on earlier devices.
//        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        delayDismiss(DISMISS_DELAY_SHORT);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        registerReceiver(dataInitializationReceiver,
//                new IntentFilter(Broadcasts.Actions.ACTION_DATA_INITIALIZED));
//        if (!AssetsState.isAssetsInitialized(this)) {
//            delayDismiss(DISMISS_DELAY_LONG);
//        } else {
//            delayDismiss(DISMISS_DELAY);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(dataInitializationReceiver);
    }

    private void delayDismiss(long delay) {
        mHandler.postDelayed(delayHideSplash, delay);
    }

    private Runnable delayHideSplash = new Runnable() {
        @Override
        public void run() {
            if (dismissed) return;
            dismissed = true;
            startHomePage();
        }
    };

    private BroadcastReceiver dataInitializationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            delayDismiss(0);
        }
    };

    private void startHomePage() {
        MainService.start(this);
        TradeService.start(this);
        SyncService.start(this);
        MainActivity.start(SplashScreenActivity.this);
        finish();
    }

}

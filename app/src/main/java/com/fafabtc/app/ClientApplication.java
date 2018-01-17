package com.fafabtc.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.fafabtc.app.di.AppInjector;
import com.fafabtc.common.analysis.AnalysisHelper;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/7.
 */

public class ClientApplication extends DaggerApplication  {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        AnalysisHelper.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return new AndroidInjector<DaggerApplication>() {
            @Override
            public void inject(DaggerApplication instance) {
                AppInjector.inject((ClientApplication) instance);
            }
        };
    }
}

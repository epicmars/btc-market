package com.fafabtc.app.di;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.fafabtc.app.ClientApplication;
import com.fafabtc.app.di.component.DaggerAppComponent;
import com.fafabtc.app.ui.base.BindLayout;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class AppInjector {

    public static void inject(ClientApplication application) {
        DaggerAppComponent.builder()
                .appContext(application)
                .build()
                .inject(application);

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                inject(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private static void inject(Activity activity) {
        BindLayout bindLayout = activity.getClass().getAnnotation(BindLayout.class);
        Injectable injectable = activity.getClass().getAnnotation(Injectable.class);
        if (injectable != null || (bindLayout != null && bindLayout.injectable())) {
            AndroidInjection.inject(activity);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
                            super.onFragmentAttached(fm, f, context);
                            BindLayout fragmentBindLayout = f.getClass().getAnnotation(BindLayout.class);
                            Injectable fragmentInjectable = f.getClass().getAnnotation(Injectable.class);
                            if (fragmentInjectable != null || (fragmentBindLayout != null && fragmentBindLayout.injectable())) {
                                AndroidSupportInjection.inject(f);
                            }
                        }
                    }, true);
        }
    }
}

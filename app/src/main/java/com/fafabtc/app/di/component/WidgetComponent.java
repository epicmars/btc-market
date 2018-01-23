package com.fafabtc.app.di.component;

import com.fafabtc.app.receiver.AssetsWidgetProvider;

import dagger.Subcomponent;

/**
 * Created by jastrelax on 2018/1/23.
 */

@Subcomponent
public interface WidgetComponent {

    void inject(AssetsWidgetProvider assetsWidgetProvider);
}

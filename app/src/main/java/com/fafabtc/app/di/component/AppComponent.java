package com.fafabtc.app.di.component;

import android.content.Context;

import com.fafabtc.app.ClientApplication;
import com.fafabtc.app.di.module.ActivityModule;
import com.fafabtc.app.di.module.AppModule;
import com.fafabtc.app.di.module.ReceiverModule;
import com.fafabtc.app.di.module.ServiceModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityModule.class,
        ServiceModule.class,
        ReceiverModule.class})
public interface AppComponent {

    void inject(ClientApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder appContext(Context context);
        AppComponent build();
    }

    ViewHolderComponent viewHolderComponent();
    WidgetComponent widgetComponent();
}

package com.fafabtc.app.di.module;


import com.fafabtc.data.di.module.DataModule;

import dagger.Module;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Module(includes = {ViewModelModule.class,
        DataModule.class})
public class AppModule {

}

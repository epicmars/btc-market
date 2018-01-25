package com.fafabtc.huobi.di;

import com.fafabtc.huobi.data.repo.HuobiPairRepo;
import com.fafabtc.huobi.data.repo.HuobiRepo;
import com.fafabtc.huobi.data.repo.HuobiTickerRepo;
import com.fafabtc.huobi.data.repo.impl.HuobiPairRepository;
import com.fafabtc.huobi.data.repo.impl.HuobiRepository;
import com.fafabtc.huobi.data.repo.impl.HuobiTickerRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Created by jastrelax on 2018/1/25.
 */
@Module(includes = {HuobiDatabaseModule.class, HuobiApiModule.class})
public abstract class HuobiModule {

    @Binds
    @Singleton
    public abstract HuobiRepo huobiRepo(HuobiRepository repository);

    @Binds
    @Singleton
    public abstract HuobiPairRepo huobiPairRepo(HuobiPairRepository repository);

    @Binds
    @Singleton
    public abstract HuobiTickerRepo huobiTickerRepo(HuobiTickerRepository repository);

}

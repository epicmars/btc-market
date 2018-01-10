package com.fafabtc.gateio.di;

import com.fafabtc.gateio.data.repo.GateioPairRepo;
import com.fafabtc.gateio.data.repo.GateioRepo;
import com.fafabtc.gateio.data.repo.GateioTickerRepo;
import com.fafabtc.gateio.data.repo.impl.GateioPairRepository;
import com.fafabtc.gateio.data.repo.impl.GateioRepository;
import com.fafabtc.gateio.data.repo.impl.GateioTickerRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Module(includes = {GateioDbModule.class, GateioApiModule.class})
public abstract class GateioModule {

    @Binds
    @Singleton
    public abstract GateioRepo gateioRepo(GateioRepository repository);

    @Binds
    @Singleton
    public abstract GateioPairRepo gateioPairRepo(GateioPairRepository repository);

    @Binds
    @Singleton
    public abstract GateioTickerRepo gateioTickerRepo(GateioTickerRepository repository);

}

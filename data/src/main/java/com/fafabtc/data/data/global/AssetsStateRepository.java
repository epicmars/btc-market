package com.fafabtc.data.data.global;

import com.fafabtc.data.model.entity.exchange.AssetsState;

import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/19.
 */
@Singleton
public class AssetsStateRepository {

    private ExchangeDatabaseProviderHelper helper;

    private final Object assetsStateLock = new Object();

    @Inject
    public AssetsStateRepository(ExchangeDatabaseProviderHelper helper) {
        this.helper = helper;
    }

    private AssetsState getAssetsState(String assetsUUID, String exchange) {
        AssetsState cached = null;
        AssetsState[] assetsStates = helper.find(AssetsState[].class,
                "assets_uuid = ? and exchange = ?",
                new String[]{assetsUUID, exchange});
        if (assetsStates != null && assetsStates.length > 0) {
            cached = assetsStates[0];
        }

        if (cached == null) {
            cached = new AssetsState();
            cached.setExchange(exchange);
            cached.setAssetsUuid(assetsUUID);
        }
        return cached;
    }

    public Single<Boolean> getAssetsInitialized(final String assetsUUID, final String exchange) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (assetsUUID == null || exchange == null)
                    throw new IllegalArgumentException();
                AssetsState assetsState = getAssetsState(assetsUUID, exchange);
                return assetsState.getAssetsInitialized() == null ? false : assetsState.getAssetsInitialized();
            }
        }).onErrorReturnItem(false);
    }

    public Completable setAssetsInitialized(final String assetsUUID, final String exchange, final Boolean assetsInitialized) {
        return Completable
                .fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (assetsUUID == null || exchange == null)
                            throw new IllegalArgumentException();
                        synchronized (assetsStateLock) {
                            AssetsState assetsState = getAssetsState(assetsUUID, exchange);
                            assetsState.setAssetsInitialized(assetsInitialized);
                            helper.insert(assetsState);
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e("setAssetsInitialized", throwable);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Timber.d("asssets: %s of exchange: %s is initialized[%b]", assetsUUID, exchange, assetsInitialized);
                    }
                });
    }

}

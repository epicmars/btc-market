package com.fafabtc.data.data.global;

import com.fafabtc.common.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/19.
 */
@Singleton
public class AssetsStateRepository {

    private SharedPreferenceDataHelper sharedDataHelper;

    private final Object assetsStateLock = new Object();

    @Inject
    public AssetsStateRepository(SharedPreferenceDataHelper repository) {
        this.sharedDataHelper = repository;
    }

    public Single<Date> getUpdateTime(final String exchange) {
        return Single.fromCallable(new Callable<Date>() {
            @Override
            public Date call() throws Exception {
                if (exchange == null) throw new IllegalArgumentException();
                Date updateTime = new Date(0);
                AssetsState[] assetsStates = sharedDataHelper.find(AssetsState.class, AssetsState[].class);
                if (assetsStates != null) {
                    for (AssetsState assetsState : assetsStates) {
                        if (exchange.equals(assetsState.getExchange())) {
                            updateTime = assetsState.getUpdateTime() == null ? updateTime : assetsState.getUpdateTime();
                            break;
                        }
                    }
                }
                return updateTime;
            }
        }).onErrorReturnItem(new Date(0));
    }

    public Completable setUpdateTime(final String exchange, final Date date, final boolean success) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (exchange == null) throw new IllegalArgumentException();
                if (!success) return;

                synchronized (assetsStateLock) {
                    AssetsState[] assetsStates = getUpdatedAssetsState(exchange, date);
                    sharedDataHelper.save(AssetsState.class, assetsStates);
                }
            }
        });
    }

    private AssetsState[] getUpdatedAssetsState(String exchange, final Date date) {
        AssetsState[] assetsStates = sharedDataHelper.find(AssetsState.class, AssetsState[].class);
        List<AssetsState> stateList = new ArrayList<>();
        AssetsState cached = null;
        if (assetsStates != null) {
            for (AssetsState assetsState : assetsStates) {
                stateList.add(assetsState);
                if (exchange.equals(assetsState.getExchange())) {
                    cached = assetsState;
                    break;
                }
            }
        }
        if (cached == null) {
            cached = new AssetsState();
            cached.setExchange(exchange);
            cached.setUpdateTime(date);
            stateList.add(cached);
        } else {
            cached.setUpdateTime(date);
        }
        assetsStates = new AssetsState[stateList.size()];
        return stateList.toArray(assetsStates);

    }

    private AssetsState[] getAssetsState(String exchange) {
        AssetsState[] assetsStates = sharedDataHelper.find(AssetsState.class, AssetsState[].class);
        List<AssetsState> stateList = new ArrayList<>();
        AssetsState cached = null;
        if (assetsStates != null) {
            for (AssetsState assetsState : assetsStates) {
                stateList.add(assetsState);
                if (exchange.equals(assetsState.getExchange())) {
                    cached = assetsState;
                    break;
                }
            }
        }
        if (cached == null) {
            cached = new AssetsState();
            cached.setExchange(exchange);
            stateList.add(cached);
        }
        assetsStates = new AssetsState[stateList.size()];
        return stateList.toArray(assetsStates);

    }

    public Single<String> getFormatedUpdateTime(String exchange) {
        return getUpdateTime(exchange)
                .map(new Function<Date, String>() {
                    @Override
                    public String apply(Date date) throws Exception {
                        long updateTime = date.getTime();
                        String formatDate = "";
                        if (updateTime > 0) {
                            formatDate = DateTimeUtils.formatStandard(new Date(updateTime));
                        }
                        return formatDate;
                    }
                });
    }

    public Single<Boolean> getAssetsInitialized(final String exchange) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (exchange == null) throw new IllegalArgumentException();
                Boolean assetsInitialized = false;
                AssetsState[] assetsStates = sharedDataHelper.find(AssetsState.class, AssetsState[].class);
                if (assetsStates != null) {
                    for (AssetsState assetsState : assetsStates) {
                        if (exchange.equals(assetsState.getExchange())) {
                            assetsInitialized = assetsState.getAssetsInitialized() == null ? assetsInitialized : assetsState.getAssetsInitialized();
                            break;
                        }
                    }
                }
                return assetsInitialized;
            }
        }).onErrorReturnItem(false);
    }

    public Completable setAssetsInitialized(final String exchange, final Boolean assetsInitialized) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (exchange == null) throw new IllegalArgumentException();

                synchronized (assetsStateLock) {
                    AssetsState[] assetsStates = getAssetsState(exchange);
                    for (AssetsState state : assetsStates) {
                        if (exchange.equals(state.getExchange())) {
                            state.setAssetsInitialized(assetsInitialized);
                            break;
                        }
                    }
                    sharedDataHelper.save(AssetsState.class, assetsStates);
                }
            }
        });
    }

    public Single<Boolean> getExchangeInitialized(final String exchange) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (exchange == null) throw new IllegalArgumentException();
                Boolean exchangeInitialized = false;
                AssetsState[] assetsStates = sharedDataHelper.find(AssetsState.class, AssetsState[].class);
                if (assetsStates != null) {
                    for (AssetsState assetsState : assetsStates) {
                        if (exchange.equals(assetsState.getExchange())) {
                            exchangeInitialized = assetsState.getExchangeInitialized() == null ? exchangeInitialized : assetsState.getExchangeInitialized();
                            break;
                        }
                    }
                }
                return exchangeInitialized;
            }
        }).onErrorReturnItem(false);
    }

    public Completable setExchangeInitialized(final String exchange, final Boolean exchangeInitialized) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (exchange == null) throw new IllegalArgumentException();

                synchronized (assetsStateLock) {
                    AssetsState[] assetsStates = getAssetsState(exchange);
                    for (AssetsState state : assetsStates) {
                        if (exchange.equals(state.getExchange())) {
                            state.setExchangeInitialized(exchangeInitialized);
                            break;
                        }
                    }
                    sharedDataHelper.save(AssetsState.class, assetsStates);
                }
            }
        });
    }
}

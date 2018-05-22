package com.fafabtc.data.data.global;

import com.fafabtc.common.utils.DateTimeUtils;
import com.fafabtc.data.model.entity.exchange.ExchangeState;

import java.util.Date;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/5/20.
 */
@Singleton
public class ExchangeStateRepository {

    private ExchangeDatabaseProviderHelper helper;

    private Object exchangeStateLock = new Object();

    @Inject
    public ExchangeStateRepository(ExchangeDatabaseProviderHelper helper) {
        this.helper = helper;
    }

    public Single<Date> getUpdateTime(final String exchange) {
        return Single.fromCallable(new Callable<Date>() {
            @Override
            public Date call() throws Exception {
                if (exchange == null) throw new IllegalArgumentException();
                Date updateTime = new Date(0);
                ExchangeState exchangeState = getExchangeState(exchange);
                updateTime = exchangeState.getUpdateTime() == null ? updateTime : exchangeState.getUpdateTime();
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

                synchronized (exchangeStateLock) {
                    ExchangeState exchangeState = getExchangeState(exchange);
                    exchangeState.setUpdateTime(date);
                    exchangeState.setExchangeInitialized(success);
                    helper.insert(exchangeState);
                }
            }
        });
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


    public Single<Boolean> getExchangeInitialized(final String exchange) {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (exchange == null) throw new IllegalArgumentException();
                Boolean exchangeInitialized = false;
                ExchangeState[] exchangeStates = helper.find(ExchangeState[].class, "exchange = ?", new String[]{exchange});
                ExchangeState exchangeState = exchangeStates[0];
                return exchangeState.getExchangeInitialized() == null ? false : exchangeState.getExchangeInitialized();
            }
        }).onErrorReturnItem(false);
    }

    public Completable setExchangeInitialized(final String exchange, final Boolean exchangeInitialized) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (exchange == null) throw new IllegalArgumentException();

                synchronized (exchangeStateLock) {
                    ExchangeState exchangeState = getExchangeState(exchange);
                    exchangeState.setExchangeInitialized(exchangeInitialized);
                    helper.insert(exchangeState);
                }
            }
        });
    }

    private ExchangeState getExchangeState(String exchange) {
        ExchangeState cached = null;
        ExchangeState[] exchangeStates = helper.find(ExchangeState[].class, "exchange = ?", new String[]{exchange});
        if (exchangeStates != null && exchangeStates.length > 0) {
            cached = exchangeStates[0];
        }
        if (cached == null) {
            cached = new ExchangeState();
            cached.setExchange(exchange);
        }
        return cached;
    }
}

package com.fafabtc.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.data.repo.OrderRepo;
import com.fafabtc.data.model.entity.exchange.Order;

import java.util.List;

import javax.inject.Inject;

import dagger.android.DaggerService;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/14.
 */

public class TradeService extends DaggerService {

    @Inject
    OrderRepo orderRepo;

    public static void start(Context context) {
        Intent starter = new Intent(context, TradeService.class);
        context.startService(starter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(tickerUpdateReceiver, new IntentFilter(DataBroadcasts.Actions.ACTION_TICKER_UPDATED));
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(tickerUpdateReceiver);
    }

    private BroadcastReceiver tickerUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String exchange = intent.getStringExtra(DataBroadcasts.Extras.EXTRA_EXCHANGE_NAME);
            // Find all pending orders, check if the order can be deal.
            orderRepo.dealPendingOrders()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<Order>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<Order> orders) {
                            if (orders.size() > 0) {
                                Intent actionDeal = new Intent(Broadcasts.Actions.ACTION_ORDER_DEAL);
                                actionDeal.putExtra(Broadcasts.Extras.EXTRAS_EXCHANGE_NAME, exchange);
                                sendBroadcast(actionDeal);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e);
                        }
                    });
        }
    };


}

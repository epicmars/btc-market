package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.PortfolioRepo;
import com.fafabtc.data.data.repo.OrderRepo;
import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.data.model.entity.exchange.Order;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/14.
 */

public class OrdersViewModel extends ViewModel {

    private String assetsUUID;
    private String exchange;
    private String pair;

    private MutableLiveData<List<Order>> ordersData = new MutableLiveData<>();

    @Inject
    PortfolioRepo portfolioRepo;

    @Inject
    OrderRepo orderRepo;

    @Inject
    public OrdersViewModel() {
    }

    public void loadOrders() {
        Single<String> assetsUuidSingle = assetsUUID == null ?
                portfolioRepo.getCurrent()
                        .map(new Function<Portfolio, String>() {
                            @Override
                            public String apply(Portfolio portfolio) throws Exception {
                                return portfolio.getUuid();
                            }
                        })
                : Single.just(assetsUUID);
        assetsUuidSingle
                .flatMap(new Function<String, SingleSource<List<Order>>>() {
                    @Override
                    public SingleSource<List<Order>> apply(String s) throws Exception {
                        return orderRepo.getOrdersOfPair(s, exchange, pair);
                    }
                })
                .compose(RxUtils.<List<Order>>singleAsyncIO())
                .subscribe(new SingleObserver<List<Order>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Order> orders) {
                        ordersData.setValue(orders);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public MutableLiveData<List<Order>> getOrdersData() {
        return ordersData;
    }

    public void setAssetsUUID(String assetsUUID) {
        this.assetsUUID = assetsUUID;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }
}

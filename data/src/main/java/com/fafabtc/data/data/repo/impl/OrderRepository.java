package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.local.dao.BlockchainAssetsDao;
import com.fafabtc.data.data.local.dao.ExchangeDao;
import com.fafabtc.data.data.local.dao.OrderDao;
import com.fafabtc.data.data.local.dao.TickerDao;
import com.fafabtc.data.data.repo.OrderRepo;
import com.fafabtc.data.model.entity.exchange.Order;
import com.fafabtc.data.model.entity.exchange.Ticker;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/9.
 */
@Singleton
public class OrderRepository implements OrderRepo {

    @Inject
    public OrderRepository() {
    }

    @Inject
    OrderDao dao;

    @Inject
    ExchangeDao exchangeDao;

    @Inject
    TickerDao tickerDao;

    @Inject
    BlockchainAssetsDao blockchainAssetsDao;

    @Override
    public Completable save(final Order order) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (order.getId() == null) {
                    // if uuid is the same, it will fail to insert.
                    dao.insertOne(order);
                } else {
                    dao.updateOne(order);
                }
            }
        });
    }

    @Override
    public Completable createNewOrder(final String assetsUUID,
                                      final String exchangeName,
                                      final double price,
                                      final double quantity,
                                      final String pair,
                                      final String base,
                                      final String quote,
                                      final Order.Type type) {
        return Single
                .fromCallable(new Callable<Order>() {
                    @Override
                    public Order call() throws Exception {
                        return dao.createNewOrder(assetsUUID,
                                exchangeName,
                                price,
                                quantity,
                                pair,
                                base,
                                quote,
                                type);
                    }
                })
                .flatMapCompletable(new Function<Order, CompletableSource>() {
                    @Override
                    public CompletableSource apply(final Order order) throws Exception {
                        return Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                Ticker ticker = tickerDao.getLatest(exchangeName, pair);
                                dao.dealPendingOrder(order, ticker);
                            }
                        }).onErrorComplete();
                    }
                });
    }

    @Override
    public Single<List<Order>> getOrdersOfPair(final String assetsUUID, final String exchange, final String pair) {
        return Single.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() throws Exception {
                return dao.findByPair(assetsUUID, exchange, pair);
            }
        });
    }

    @Override
    public Single<List<Order>> getOrdersOfExchange(final String assetsUUID, final String exchange) {
        return Single.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() throws Exception {
                return dao.findByExchage(assetsUUID, exchange);
            }
        });
    }

    @Override
    public Single<List<Order>> getAllPendingOrder() {
        return Single.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() throws Exception {
                return dao.findAllByState(Order.State.PENDING.name());
            }
        });
    }

    @Override
    public Single<List<Order>> dealPendingOrders() {
        return getAllPendingOrder()
                .flattenAsObservable(new Function<List<Order>, Iterable<Order>>() {
                    @Override
                    public Iterable<Order> apply(List<Order> orders) throws Exception {
                        return orders;
                    }
                })
                .flatMapMaybe(new Function<Order, MaybeSource<Order>>() {
                    @Override
                    public MaybeSource<Order> apply(final Order order) throws Exception {
                        return Maybe.fromCallable(new Callable<Order>() {
                            @Override
                            public Order call() throws Exception {
                                Ticker ticker = tickerDao.getLatest(order.getExchange(), order.getPair());
                                dao.dealPendingOrder(order, ticker);
                                return order;
                            }
                        }).onErrorComplete();
                    }
                }).toList();
    }

    @Override
    public Completable cancelOrder(final String orderUUID) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                dao.cancelOrder(orderUUID);
            }
        });
    }
}

package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.Order;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/9.
 */

public interface OrderRepo {

    Completable save(Order order);

    Completable createNewOrder(final String assetsUUID,
                               final String exchangeName,
                               final double price,
                               final double quantity,
                               final String pair,
                               final String base,
                               final String quote,
                               final Order.Type type);

    Completable cancelOrder(String orderUUID);

    Single<List<Order>> getOrdersOfPair(String assetsUUID, String exchange, String pair);

    Single<List<Order>> getOrdersOfExchange(String assetsUUID, String exchange);

    Single<List<Order>> getAllPendingOrder();

    Completable dealPendingOrders();
}

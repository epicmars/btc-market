package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.fafabtc.common.utils.UUIDUtils;
import com.fafabtc.data.exception.DaoException;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.entity.exchange.Order;
import com.fafabtc.data.model.entity.exchange.Ticker;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/9.
 */
@Dao
public abstract class OrderDao {

    @Insert
    public abstract long insertOne(Order order);

    @Update
    public abstract int updateOne(Order order);

    @Query("select * from orders where uuid = :uuid limit 1")
    public abstract Order findByUUID(String uuid);

    @Query("select * from orders where assets_uuid = :assetsUUID and exchange = :exchange and pair = :pair")
    public abstract List<Order> findByPair(String assetsUUID, String exchange, String pair);

    @Query("select * from orders where assets_uuid = :assetsUUID and exchange = :exchange")
    public abstract List<Order> findByExchage(String assetsUUID, String exchange);

    @Query("select * from orders where state = :state COLLATE NOCASE")
    public abstract List<Order> findAllByState(String state);

    @Update
    public abstract void updateBlockchainAssets(BlockchainAssets... blockchainAssets);

    @Query("select * from blockchain_assets where assets_uuid = :assetsUUID and exchange = :exchange and name = :name limit 1")
    public abstract BlockchainAssets findBlockchainAssets(String assetsUUID, String exchange, String name);

    @Query("select * from exchange where name = :name limit 1")
    public abstract Exchange findExchange(String name);

    @Transaction
    public Order createNewOrder(String assetsUUID,
                               String exchangeName,
                               double price,
                               double quantity,
                               String pair,
                               String base,
                               String quote,
                               Order.Type type) {
        // get exchange
        Exchange exchange = findExchange(exchangeName);
        final double volume = price * quantity;

        double lockVolume = quantity;
        String lockAssetsName = base;
        double commission = volume * exchange.getCommissionRate();
        String commissionAsset = quote;
        if (Order.Type.BUY == type) {
            lockVolume = volume;
            lockAssetsName = quote;
            commission = quantity * exchange.getCommissionRate();
            commissionAsset = base;
        }

        // Check if there is enough assets.
        BlockchainAssets lockAssets = findBlockchainAssets(assetsUUID, exchangeName, lockAssetsName);
        if (lockAssets.getAvailable() < lockVolume) {
            throw new DaoException(String.format("%s %s : available %s is not sufficient.", type.name(), base, lockAssetsName));
        }

        // Update blockchain assets.
        lockBlockchainAssets(lockAssets, lockVolume);
        // Save order.
        return saveNewOrder(assetsUUID,
                exchangeName,
                price,
                quantity,
                pair,
                base,
                quote,
                type,
                commission,
                commissionAsset);
    }

    @Transaction
    public void dealPendingOrder(Order order, Ticker ticker) {
        if (order.getState() != Order.State.PENDING) return;
        final double volume = order.getQuantity() * order.getPrice();
        final String assetsUUID = order.getAssetsUUID();
        final String exchange = order.getExchange();
        BlockchainAssets base = findBlockchainAssets(assetsUUID, exchange, order.getBase());
        BlockchainAssets quote = findBlockchainAssets(assetsUUID, exchange, order.getQuote());

        // if sell
        double priceBid = ticker.getBid();
        double priceAsk = order.getPrice();
        BlockchainAssets in = quote;
        BlockchainAssets out = base;
        double lockedVolume = order.getQuantity();
        double incomeVolume = volume;
        // if buy
        if (order.getType() == Order.Type.BUY) {
            priceBid = order.getPrice();
            priceAsk = ticker.getAsk();
            in = base;
            out = quote;
            lockedVolume = volume;
            incomeVolume = order.getQuantity();
        }
        if (priceAsk <= priceBid) {
            // Deal.
            // out assets is locked, so free it.
            out.setLocked(out.getLocked() - lockedVolume);
            if (!in.getName().equalsIgnoreCase(order.getCommissionAsset())) {
                throw new DaoException(String.format("Incoming assets is [%s], while commission assets is [%s].", in.getName(), order.getCommissionAsset()));
            }
            in.setAvailable(in.getAvailable() + incomeVolume - order.getCommission());
            updateBlockchainAssets(out, in);
            order.setState(Order.State.DONE);
            updateOne(order);
        }
    }

    @Transaction
    public void cancelOrder(String uuid) {
        Order order = findByUUID(uuid);
        if (order.getState() == Order.State.CANCELLED || order.getState() == Order.State.DONE)
            return;
        BlockchainAssets base = findBlockchainAssets(order.getAssetsUUID(), order.getExchange(), order.getBase());
        BlockchainAssets quote = findBlockchainAssets(order.getAssetsUUID(), order.getExchange(), order.getQuote());

        boolean isSelling = order.getType() == Order.Type.SELL;
        BlockchainAssets lockedAssets = isSelling ? base : quote;
        double lockedVolume = isSelling ? order.getQuantity() : order.getQuantity() * order.getPrice();
        lockedAssets.setLocked(lockedAssets.getLocked() - lockedVolume);
        lockedAssets.setAvailable(lockedAssets.getAvailable() + lockedVolume);
        updateBlockchainAssets(lockedAssets);

        order.setState(Order.State.CANCELLED);
        updateOne(order);
    }

    private void lockBlockchainAssets(BlockchainAssets assets, double lockedVolume) {
        assets.setAvailable(assets.getAvailable() - lockedVolume);
        assets.setLocked(assets.getLocked() + lockedVolume);
        updateBlockchainAssets(assets);
    }

    private Order saveNewOrder(String assetsUUID,
                              String exchangeName,
                              double price,
                              double quantity,
                              String pair,
                              String base,
                              String quote,
                              Order.Type type,
                              double commission,
                              String commissionAsset) {
        // create order
        Order order = new Order();
        order.setType(type);
        order.setAssetsUUID(assetsUUID);
        order.setExchange(exchangeName);
        order.setUuid(UUIDUtils.newUUID());
        order.setPair(pair);
        order.setBase(base);
        order.setQuote(quote);
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setCommission(commission);
        order.setCommissionAsset(commissionAsset);
        insertOne(order);
        return order;
    }
}

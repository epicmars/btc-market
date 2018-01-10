package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.BalanceAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Deprecated
public interface BalanceAssetsRepo {

    Completable save(BalanceAssets balanceAssets);

    Completable update(BalanceAssets balanceAssets);

    Single<BalanceAssets> getCurrentBalanceAssets(String exchange, String name);

    Single<BalanceAssets> getBalanceAssets(String assetsUUID, String exchange, String name);

    Single<List<BalanceAssets>> getAccountBalanceAssets(String assetsUUID);

    Single<List<BalanceAssets>> getExchangeBalanceAssets(String assetsUUID, String exchange);

    Completable initExchangeBalanceAssets(String assetsUUID, Exchange exchange);
}

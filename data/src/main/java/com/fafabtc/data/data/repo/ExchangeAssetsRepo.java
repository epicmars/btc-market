package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.vo.ExchangeAssets;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface ExchangeAssetsRepo {

    Completable init();

    Completable initExchange(String exchange);

    Completable initExchangeAssets(final String exchange);

    Single<Boolean> isExchangeAssetsInitialized();

    Single<Boolean> isExchangeAssetsInitialized(String exchange);

    Single<AccountAssets> createAccountAssetsOfExchange(final String assetsName);

    Single<AccountAssets> createAccountAssetsOfExchange(final String assetsName, final String exchange);

    Single<List<ExchangeAssets>> getAllExchangeAssetsOfCurrentAccount();

    Single<List<ExchangeAssets>> getAllExchangeAssetsOfAccount(AccountAssets accountAssets);

    Single<List<ExchangeAssets>> getAllExchangeAssetsOfExchange(Exchange exchange);

    Single<ExchangeAssets> getExchangeAssets(AccountAssets accountAssets, Exchange exchange);

    Completable cacheExchangeAssetsToFile(final Exchange exchange);
}

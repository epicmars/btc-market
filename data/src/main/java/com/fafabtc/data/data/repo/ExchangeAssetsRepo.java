package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.vo.ExchangeAssets;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface ExchangeAssetsRepo {

    String DIR_DOCUMENTS = "Documents";
    String DIR_DEFAULT = "fafabtc";

    Completable init();

    Completable initExchange(String exchange);

    Completable initExchangeAssets(final String exchange);

    Single<Boolean> hasExchangeInitialized();

    Single<Boolean> hasExchangeAssetsInitialized();

    Single<Boolean> isExchangeAssetsInitialized(String exchange);

    Single<Portfolio> createPortfolioOfExchange(final String assetsName);

    Single<Portfolio> createPortfolioOfExchange(final String assetsName, final String exchange);

    Single<List<ExchangeAssets>> getAllExchangeAssetsOfCurrentAccount();

    Single<List<ExchangeAssets>> getAllExchangeAssetsOfAccount(Portfolio portfolio);

    Single<List<ExchangeAssets>> getAllExchangeAssetsOfExchange(Exchange exchange);

    Single<ExchangeAssets> getExchangeAssets(Portfolio portfolio, Exchange exchange);

    Completable cacheExchangeAssetsToFile(final Exchange exchange);
}

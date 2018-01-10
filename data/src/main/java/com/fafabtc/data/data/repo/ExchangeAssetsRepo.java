package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.vo.AccountAssetsData;
import com.fafabtc.data.model.vo.ExchangeAssets;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface ExchangeAssetsRepo {

    Completable restoreExchangeAssets(ExchangeAssets assets);

    Completable initExchangeAssets(ExchangeAssets assets);

    Single<List<ExchangeAssets>> getAllExchangeAssetsOfCurrentAccount();

    Single<List<ExchangeAssets>> getAllExchangeAssetsOfAccount(AccountAssets accountAssets);

    Single<ExchangeAssets> getExchangeAssets(AccountAssets accountAssets, Exchange exchange);

    Single<List<AccountAssetsData>> getAllAccountAssetsData();
}

package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.vo.ExchangeAssets;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface BlockchainAssetsRepo {

    Completable save(BlockchainAssets blockchainAssets);

    Completable update(BlockchainAssets blockchainAssets);

    Completable initExchangeBlockchainAssets(String assetsUUID, String exchangeName);

    Completable restoreExchangeBlockchainAssets(ExchangeAssets exchangeAssets);

    Single<Double> getUsdtBalanceFromAccount(String assetsUUID);

    Single<List<BlockchainAssets>> getBalanceFromAccount(String assetsUUID);

    Single<BlockchainAssets> getFromCurrentPortfolio(String exchangeName, String name);

    Single<List<BlockchainAssets>> getBaseBlockchainAssets(String assetsUUID, String exchangeName);

    Single<List<BlockchainAssets>> getQuoteBlockchainAssets(String assetsUUID, String exchangeName);

    Single<List<BlockchainAssets>> getFromAccountByName(String assetsUUID, String name);

    Single<List<BlockchainAssets>> getAllFromExchange(String assetsUUID, String exchangeName);
}

package com.fafabtc.data.data.repo.impl;

import android.support.v4.util.Pair;

import com.fafabtc.data.data.local.dao.AccountAssetsDao;
import com.fafabtc.data.data.local.dao.ExchangeDao;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.vo.AccountAssetsData;
import com.fafabtc.data.model.vo.ExchangeAssets;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Singleton
public class ExchangeAssetsRepository implements ExchangeAssetsRepo {

    @Inject
    BlockchainAssetsRepo blockchainAssetsRepo;

    @Inject
    ExchangeDao exchangeDao;

    @Inject
    AccountAssetsDao accountAssetsDao;

    @Inject
    public ExchangeAssetsRepository() {
    }

    @Override
    public Completable initExchangeAssets(ExchangeAssets assets) {
        AccountAssets accountAssets = assets.getAccountAssets();
        Exchange exchange = assets.getExchange();
        return blockchainAssetsRepo.initExchangeBlockchainAssets(accountAssets.getUuid(), exchange.getName());
    }

    @Override
    public Completable restoreExchangeAssets(ExchangeAssets assets) {
        return blockchainAssetsRepo.restoreExchangeBlockchainAssets(assets);
    }

    @Override
    public Single<List<ExchangeAssets>> getAllExchangeAssetsOfAccount(AccountAssets accountAssets) {
        return Single.just(accountAssets)
                .flatMapObservable(new Function<AccountAssets, ObservableSource<Pair<AccountAssets, Exchange>>>() {
                    @Override
                    public ObservableSource<Pair<AccountAssets, Exchange>> apply(final AccountAssets accountAssets) throws Exception {
                        return Observable.fromArray(exchangeDao.findAll())
                                .map(new Function<Exchange, Pair<AccountAssets, Exchange>>() {
                                    @Override
                                    public Pair<AccountAssets, Exchange> apply(Exchange exchange) throws Exception {
                                        return new Pair<>(accountAssets, exchange);
                                    }
                                });
                    }
                }).flatMap(new Function<Pair<AccountAssets, Exchange>, ObservableSource<ExchangeAssets>>() {
                    @Override
                    public ObservableSource<ExchangeAssets> apply(Pair<AccountAssets, Exchange> accountAssetsExchangePair) throws Exception {
                        return getExchangeAssets(accountAssetsExchangePair.first, accountAssetsExchangePair.second).toObservable();
                    }
                }).toList();
    }

    @Override
    public Single<List<ExchangeAssets>> getAllExchangeAssetsOfCurrentAccount() {
        return Single
                .fromCallable(new Callable<AccountAssets>() {
                    @Override
                    public AccountAssets call() throws Exception {
                        return accountAssetsDao.findCurrent();
                    }
                }).flatMap(new Function<AccountAssets, SingleSource<? extends List<ExchangeAssets>>>() {
                    @Override
                    public SingleSource<? extends List<ExchangeAssets>> apply(AccountAssets accountAssets) throws Exception {
                        return getAllExchangeAssetsOfAccount(accountAssets);
                    }
                });
    }

    @Override
    public Single<ExchangeAssets> getExchangeAssets(AccountAssets accountAssets, Exchange exchange) {
        final ExchangeAssets exchangeAssets = new ExchangeAssets();
        exchangeAssets.setAccountAssets(accountAssets);
        exchangeAssets.setExchange(exchange);
        String assetUUID = accountAssets.getUuid();
        String exchangeName = exchange.getName();
        return blockchainAssetsRepo
                .getBaseBlockchainAssets(assetUUID, exchangeName)
                .zipWith(blockchainAssetsRepo.getQuoteBlockchainAssets(assetUUID, exchangeName),
                        new BiFunction<List<BlockchainAssets>, List<BlockchainAssets>, ExchangeAssets>() {
                            @Override
                            public ExchangeAssets apply(List<BlockchainAssets> blockchainAssets,
                                                        List<BlockchainAssets> blockchainAssets2) throws Exception {
                                exchangeAssets.setBlockchainAssetsList(blockchainAssets);
                                exchangeAssets.setQuoteAssetsList(blockchainAssets2);
                                return exchangeAssets;
                            }
                        });
    }

    @Override
    public Single<List<AccountAssetsData>> getAllAccountAssetsData() {
        return Single.fromCallable(new Callable<List<AccountAssets>>() {
            @Override
            public List<AccountAssets> call() throws Exception {
                return accountAssetsDao.findAll();
            }
        }).flattenAsObservable(new Function<List<AccountAssets>, Iterable<AccountAssets>>() {
            @Override
            public Iterable<AccountAssets> apply(List<AccountAssets> accountAssets) throws Exception {
                return accountAssets;
            }
        }).flatMap(new Function<AccountAssets, ObservableSource<AccountAssetsData>>() {
            @Override
            public ObservableSource<AccountAssetsData> apply(final AccountAssets accountAssets) throws Exception {
                return getAllExchangeAssetsOfAccount(accountAssets)
                        .map(new Function<List<ExchangeAssets>, AccountAssetsData>() {
                            @Override
                            public AccountAssetsData apply(List<ExchangeAssets> exchangeAssets) throws Exception {
                                AccountAssetsData data = new AccountAssetsData();
                                data.setAccountAssets(accountAssets);
                                data.setExchangeAssets(exchangeAssets);
                                return data;
                            }
                        }).toObservable();
            }
        }).toList();
    }
}

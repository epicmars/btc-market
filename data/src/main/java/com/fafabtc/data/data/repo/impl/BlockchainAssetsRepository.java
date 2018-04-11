package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.local.dao.AccountAssetsDao;
import com.fafabtc.data.data.local.dao.BlockchainAssetsDao;
import com.fafabtc.data.data.local.dao.PairDao;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.vo.ExchangeAssets;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Singleton
public class BlockchainAssetsRepository implements BlockchainAssetsRepo {

    @Inject
    BlockchainAssetsDao blockchainAssetsDao;

    @Inject
    AccountAssetsDao accountAssetsDao;

    @Inject
    PairDao pairDao;

    @Inject
    BlockchainAssetsRepository() {

    }

    @Override
    public Completable save(final BlockchainAssets blockchainAssets) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                blockchainAssetsDao.insert(blockchainAssets);
            }
        });
    }

    @Override
    public Completable update(final BlockchainAssets blockchainAssets) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                blockchainAssetsDao.update(blockchainAssets);
            }
        });
    }

    private Observable<String> pairBases(final String exchangeName) {
        return Observable.fromCallable(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                return pairDao.findBases(exchangeName);
            }
        }).flatMapIterable(new Function<String[], Iterable<String>>() {
            @Override
            public Iterable<String> apply(String[] strings) throws Exception {
                return Arrays.asList(strings);
            }
        });
    }

    private Observable<String> pairQuotes(final String exchangeName) {
        return Observable.fromCallable(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                return pairDao.findQuotes(exchangeName);
            }
        }).flatMapIterable(new Function<String[], Iterable<String>>() {
            @Override
            public Iterable<String> apply(String[] strings) throws Exception {
                return Arrays.asList(strings);
            }
        });
    }

    @Override
    public Completable initExchangeBlockchainAssets(final String assetsUUID, final String exchangeName) {
        return pairBases(exchangeName)
                .concatWith(Observable.fromArray(pairDao.findQuotes(exchangeName)))
                .distinct()
                .flatMapCompletable(new Function<String, CompletableSource>() {
                    @Override
                    public CompletableSource apply(final String s) throws Exception {
                        return Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                final BlockchainAssets assets = new BlockchainAssets();
                                assets.setAssetsUUID(assetsUUID);
                                assets.setName(s);
                                assets.setExchange(exchangeName);
                                blockchainAssetsDao.insert(assets);
                            }
                        }).onErrorComplete();
                    }
                });
    }

    /**
     * Restore block chain assets of an exchange from an {@link ExchangeAssets}.
     * @param exchangeAssets
     *              assets belong to an exchange and an assets account.
     * @return a Completable
     */
    @Override
    public Completable restoreExchangeBlockchainAssets(ExchangeAssets exchangeAssets) {
        return Observable.fromIterable(exchangeAssets.getQuoteAssetsList())
                .concatWith(Observable.fromIterable(exchangeAssets.getBlockchainAssetsList()))
                .flatMapCompletable(new Function<BlockchainAssets, CompletableSource>() {
                    @Override
                    public CompletableSource apply(final BlockchainAssets blockchainAssets) throws Exception {
                        return Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                blockchainAssets.setAvailable(blockchainAssets.getAvailable() + blockchainAssets.getLocked());
                                blockchainAssets.setLocked(0.0);
                                blockchainAssetsDao.update(blockchainAssets);
                            }
                        }).onErrorComplete();
                    }
                });
    }

    @Override
    public Single<List<BlockchainAssets>> getBaseBlockchainAssets(final String assetsUUID, final String exchangeName) {
        return pairBases(exchangeName)
                .flatMapMaybe(new Function<String, MaybeSource<BlockchainAssets>>() {
                    @Override
                    public MaybeSource<BlockchainAssets> apply(final String s) throws Exception {
                        return Maybe.fromCallable(new Callable<BlockchainAssets>() {
                            @Override
                            public BlockchainAssets call() throws Exception {
                                return blockchainAssetsDao.find(assetsUUID, exchangeName, s);
                            }
                        });
                    }
                })
                .toList();
    }

    @Override
    public Single<List<BlockchainAssets>> getQuoteBlockchainAssets(final String assetsUUID, final String exchangeName) {
        return pairQuotes(exchangeName)
                .map(new Function<String, BlockchainAssets>() {
                    @Override
                    public BlockchainAssets apply(String s) throws Exception {
                        return blockchainAssetsDao.find(assetsUUID, exchangeName, s);
                    }
                })
                .toList();
    }

    @Override
    public Single<List<BlockchainAssets>> getAllFromExchange(final String assetsUUID, final String exchangeName) {
        return Single.fromCallable(new Callable<List<BlockchainAssets>>() {
            @Override
            public List<BlockchainAssets> call() throws Exception {
                return blockchainAssetsDao.findByAccountWithExchange(assetsUUID, exchangeName);
            }
        });
    }

    @Override
    public Single<BlockchainAssets> getFromCurrentAccount(final String exchangeName, final String name) {
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                return accountAssetsDao.findCurrent();
            }
        }).map(new Function<AccountAssets, BlockchainAssets>() {
            @Override
            public BlockchainAssets apply(AccountAssets accountAssets) throws Exception {
                return blockchainAssetsDao.find(accountAssets.getUuid(), exchangeName, name);
            }
        });
    }

    @Override
    public Single<List<BlockchainAssets>> getFromAccountByName(final String assetsUUID, final String name) {
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                return accountAssetsDao.findByUUID(assetsUUID);
            }
        }).map(new Function<AccountAssets, List<BlockchainAssets>>() {
            @Override
            public List<BlockchainAssets> apply(AccountAssets accountAssets) throws Exception {
                return blockchainAssetsDao.findByAccountWithName(accountAssets.getUuid(), name);
            }
        });
    }

    @Override
    public Single<Double> getUsdtBalanceFromAccount(String assetsUUID) {
        return getFromAccountByName(assetsUUID, "usdt")
                .flattenAsObservable(new Function<List<BlockchainAssets>, Iterable<BlockchainAssets>>() {
                    @Override
                    public Iterable<BlockchainAssets> apply(List<BlockchainAssets> blockchainAssets) throws Exception {
                        return blockchainAssets;
                    }
                })
                .reduce(0.0, new BiFunction<Double, BlockchainAssets, Double>() {
                    @Override
                    public Double apply(Double aDouble, BlockchainAssets blockchainAssets) throws Exception {
                        return aDouble + blockchainAssets.getAvailable() + blockchainAssets.getLocked();
                    }
                });

    }

    private Single<String[]> quotesAsBalance = Single.fromCallable(new Callable<String[]>() {
        @Override
        public String[] call() throws Exception {
            return pairDao.findQuotesAsBalance();
        }
    });

    @Override
    public Single<List<BlockchainAssets>> getBalanceFromAccount(final String assetsUUID) {
        return quotesAsBalance.flattenAsObservable(new Function<String[], Iterable<String>>() {
            @Override
            public Iterable<String> apply(String[] strings) throws Exception {
                return Arrays.asList(strings);
            }
        }).flatMap(new Function<String, ObservableSource<BlockchainAssets>>() {
            @Override
            public ObservableSource<BlockchainAssets> apply(String s) throws Exception {
                return Observable.fromIterable(blockchainAssetsDao.findByAccountWithName(assetsUUID, s));
            }
        }).toList();
    }
}

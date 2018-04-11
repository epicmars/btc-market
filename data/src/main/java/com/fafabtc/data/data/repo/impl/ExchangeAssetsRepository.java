package com.fafabtc.data.data.repo.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;

import com.fafabtc.common.file.AndroidAssetsUtils;
import com.fafabtc.common.file.FileUtils;
import com.fafabtc.common.json.GsonHelper;
import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.data.global.AssetsStateRepository;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.data.repo.ExchangeRepo;
import com.fafabtc.data.data.repo.PairRepo;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.vo.ExchangeAssets;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.fafabtc.data.provider.Providers.ASSETS_DATA_FILE;

/**
 * The repository of exchange assets.
 * <p>
 * To separate the initialization of different exchange。
 * <p>
 * Created by jastrelax on 2018/1/8.
 */
@Singleton
public class ExchangeAssetsRepository implements ExchangeAssetsRepo {

    @Inject
    BlockchainAssetsRepo blockchainAssetsRepo;

    @Inject
    AccountAssetsRepo accountAssetsRepo;

    @Inject
    ExchangeRepo exchangeRepo;

    @Inject
    AssetsStateRepository assetsStateRepository;

    @Inject
    TickerRepo tickerRepo;

    @Inject
    Context context;

    @Inject
    PairRepo pairRepo;

    @Inject
    public ExchangeAssetsRepository() {
    }

    /**
     * Initiate all exchanges and their assets.
     *
     * @return a Completable
     */
    @Override
    public Completable init() {
        return Observable.fromArray(ExchangeRepo.EXCHANGES)
                .flatMapCompletable(new Function<String, CompletableSource>() {
                    @Override
                    public CompletableSource apply(String s) throws Exception {
                        return initExchange(s).subscribeOn(Schedulers.io());
                    }
                }, true);
    }

    /**
     * Initiate a exchange and it's assets.
     *
     * @param exchangeName an exchange name
     * @return a Completable
     */
    @Override
    public Completable initExchange(final String exchangeName) {
        Completable initExchange = exchangeRepo.init(exchangeName)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        sendExchangeBroadcast(exchangeName, DataBroadcasts.Actions.ACTION_INITIATE_EXCHANGE, null);
                    }
                });
        Completable initExchangeAssets = initExchangeAssets(exchangeName)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        sendExchangeBroadcast(exchangeName, DataBroadcasts.Actions.ACTION_INITIATE_ASSETS, null);
                    }
                })
                .concatWith(assetsStateRepository.setAssetsInitialized(exchangeName, true));

        return initExchange
                .concatWith(initExchangeAssets)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        sendExchangeBroadcast(exchangeName, DataBroadcasts.Actions.ACTION_DATA_INITIALIZED, null);
                    }
                })
                .concatWith(tickerRepo.getLatestTickers(exchangeName, new Date()).toCompletable());
    }

    /**
     * Initiate assets of a exchange.
     * <p>
     * Every blockchain assets belong to a {@link AccountAssets} and a {@link Exchange},
     * If assets of a exchange is already initiated, which means the blockchain assets of
     * all assets account of an exchange have been initiated.
     * <p>
     * If assets of an exchange is already initialized, initiate it again in case of outdated
     * or missed blockchain assets item. Otherwise initiate it and restore the blockchain assets
     * from file with default or saved assets.
     *
     * @param exchangeName Name of exchange to be initiated.
     * @return a Completable
     * @see #doInitExchangeAssets(String)
     */
    @Override
    public Completable initExchangeAssets(final String exchangeName) {
        return isExchangeAssetsInitialized(exchangeName)
                .flatMapCompletable(new Function<Boolean, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Boolean isInitialized) throws Exception {
                        if (isInitialized) {
                            return doInitExchangeAssets(exchangeName);
                        } else {
                            return doInitExchangeAssets(exchangeName)
                                    .concatWith(restoreExchangeAssetsFromFile(exchangeName));
                        }
                    }
                });
    }

    /**
     * Tell whether the blockchain assets of a exchange has been initialized, which
     * means all base blockchain assets of the exchange pairs is initialized.
     *
     * @param exchange the exchange name
     * @return a Single
     */
    @Override
    public Single<Boolean> isExchangeAssetsInitialized(final String exchange) {
        final SingleTransformer<List<AccountAssets>, Boolean> transformer = new SingleTransformer<List<AccountAssets>, Boolean>() {
            @Override
            public SingleSource<Boolean> apply(Single<List<AccountAssets>> upstream) {
                return upstream
                        .flattenAsObservable(new Function<List<AccountAssets>, Iterable<AccountAssets>>() {
                            @Override
                            public Iterable<AccountAssets> apply(List<AccountAssets> accountAssets) throws Exception {
                                return accountAssets;
                            }
                        })
                        .zipWith(exchangeRepo.getExchange(exchange).toObservable(),
                                new BiFunction<AccountAssets, Exchange, Pair<AccountAssets, Exchange>>() {
                                    @Override
                                    public Pair<AccountAssets, Exchange> apply(AccountAssets accountAssets, Exchange exchange) {
                                        return new Pair<>(accountAssets, exchange);
                                    }
                                })
                        .flatMapSingle(new Function<Pair<AccountAssets, Exchange>, SingleSource<ExchangeAssets>>() {
                            @Override
                            public SingleSource<ExchangeAssets> apply(Pair<AccountAssets, Exchange> accountAssetsExchangePair) throws Exception {
                                return getExchangeAssets(accountAssetsExchangePair.first, accountAssetsExchangePair.second);
                            }
                        })
                        .zipWith(pairRepo.baseCount(exchange).toObservable(),
                                new BiFunction<ExchangeAssets, Integer, Boolean>() {
                                    @Override
                                    public Boolean apply(ExchangeAssets exchangeAssets, Integer integer) throws Exception {
                                        return exchangeAssets.getBlockchainAssetsList().size() > 0 && exchangeAssets.getBlockchainAssetsList().size() == integer;
                                    }
                                })
                        .toList()
                        .map(new Function<List<Boolean>, Boolean>() {
                            @Override
                            public Boolean apply(List<Boolean> booleans) throws Exception {
                                for (Boolean initialized : booleans) {
                                    if (!initialized)
                                        return false;
                                }
                                return true;
                            }
                        });
            }
        };
        return accountAssetsRepo.getAll()
                .flatMap(new Function<List<AccountAssets>, SingleSource<? extends Boolean>>() {
                    @Override
                    public SingleSource<? extends Boolean> apply(List<AccountAssets> accountAssets) throws Exception {
                        if (accountAssets.isEmpty())
                            return Single.just(false);
                        return Single.just(accountAssets).compose(transformer);
                    }
                });
    }

    /**
     * Restore assets from saved files or assets files.
     * <p>
     * <strong>Restored blockchain assets may be outdated and not consistent with latest data.</strong>
     * <p>
     * Firstly, try to restore assets from files in local storage, if not exist, then try restore
     * from assets data file packaged in the APK.
     *
     * @param exchangeName name of an exchange
     * @return a Completable
     */
    private Completable restoreExchangeAssetsFromFile(final String exchangeName) {
        final String fileName = exchangeName.toLowerCase() + ASSETS_DATA_FILE;
        return Observable
                .fromCallable(new Callable<ExchangeAssets[]>() {
                    @Override
                    public ExchangeAssets[] call() throws Exception {
                        return GsonHelper.gson().fromJson(FileUtils.readFile(fileName), ExchangeAssets[].class);
                    }
                })
                .onErrorReturn(new Function<Throwable, ExchangeAssets[]>() {
                    @Override
                    public ExchangeAssets[] apply(Throwable throwable) throws Exception {
                        return GsonHelper.gson().fromJson(AndroidAssetsUtils.readFromAssets(context.getAssets(), fileName), ExchangeAssets[].class);
                    }
                })
                .flatMapIterable(new Function<ExchangeAssets[], Iterable<ExchangeAssets>>() {
                    @Override
                    public Iterable<ExchangeAssets> apply(ExchangeAssets[] exchangeAssets) throws Exception {
                        return Arrays.asList(exchangeAssets);
                    }
                })
                .flatMapCompletable(new Function<ExchangeAssets, CompletableSource>() {
                    @Override
                    public CompletableSource apply(final ExchangeAssets exchangeAssets) throws Exception {
                        return restoreExchangeAssets(exchangeAssets);
                    }
                });
    }

    /**
     * Restore an exchange assets.
     *
     * @param exchangeAssets the exchangeAssets to be restored.
     * @return a Completable
     */
    private Completable restoreExchangeAssets(final ExchangeAssets exchangeAssets) {
        final String accountAssetsName = exchangeAssets.getAccountAssets().getName();
        Single<AccountAssets> accountAssets = accountAssetsRepo.getByName(accountAssetsName)
                .onErrorResumeNext(createAccountAssetsOfExchange(accountAssetsName, exchangeAssets.getExchange().getName()));
        return accountAssets
                .flatMapCompletable(new Function<AccountAssets, CompletableSource>() {
                    @Override
                    public CompletableSource apply(AccountAssets accountAssets) throws Exception {
                        exchangeAssets.setAccountAssets(accountAssets);
                        return blockchainAssetsRepo.restoreExchangeBlockchainAssets(exchangeAssets);
                    }
                })
                .onErrorComplete();
    }

    /**
     * Initiate account assets of an exchange.
     * <p>
     * Every blockchain assets belong to a {@link AccountAssets} and a {@link Exchange},
     * If assets of a exchange is already initiated, which means the blockchain assets of
     * all assets account of an exchange have been initiated.
     * <p>
     * If no account assets exist then create a new one with a default name.
     * Otherwise initiate all the account assets of specified exchange.
     *
     * @param exchange the exchange whose assets to be initialized
     * @return a Completable
     */
    private Completable doInitExchangeAssets(final String exchange) {
        return accountAssetsRepo.getAll()
                .flatMapCompletable(new Function<List<AccountAssets>, CompletableSource>() {
                    @Override
                    public CompletableSource apply(List<AccountAssets> accountAssets) throws Exception {
                        if (accountAssets.isEmpty()) {
                            return createAccountAssetsOfExchange(AccountAssets.DEFAULT_NAME, exchange).toCompletable();
                        } else {
                            return Observable.fromIterable(accountAssets)
                                    .flatMapCompletable(new Function<AccountAssets, CompletableSource>() {
                                        @Override
                                        public CompletableSource apply(AccountAssets accountAssets) throws Exception {
                                            return initAccountAssetsOfExchange(accountAssets, exchange).toCompletable();
                                        }
                                    });
                        }
                    }
                });
    }


    /**
     * Create account assets which belongs to an exchange.
     * <p>
     * When creating a account assets, set it as the current active account.
     * Then initiate it.
     * <p>
     * If the account assets already existed, no need to save it again. If current
     * account assets is not the account assets to be created, update it's state to
     * {@link AccountAssets.State#ACTIVE}.
     *
     * @param assetsName name of the account to be created
     * @param exchange   name of the exchange
     * @return a Single
     * @see #initAccountAssetsOfExchange(AccountAssets, String)
     */
    @Override
    public Single<AccountAssets> createAccountAssetsOfExchange(final String assetsName, final String exchange) {
        return accountAssetsRepo.create(assetsName)
                .flatMap(new Function<AccountAssets, SingleSource<AccountAssets>>() {
                    @Override
                    public SingleSource<AccountAssets> apply(AccountAssets accountAssets) throws Exception {
                        return initAccountAssetsOfExchange(accountAssets, exchange);
                    }
                });
    }

    /**
     * Create a new account assets and initiate all exchange assets which
     * belong to it.
     *
     * @param assetsName name of the account assets to be created.
     * @return a Single
     */
    @Override
    public Single<AccountAssets> createAccountAssetsOfExchange(final String assetsName) {
        return exchangeRepo.getExchanges()
                .flattenAsObservable(new Function<Exchange[], Iterable<Exchange>>() {
                    @Override
                    public Iterable<Exchange> apply(Exchange[] exchanges) throws Exception {
                        return Arrays.asList(exchanges);
                    }
                })
                .flatMapSingle(new Function<Exchange, SingleSource<AccountAssets>>() {
                    @Override
                    public SingleSource<AccountAssets> apply(Exchange exchange) throws Exception {
                        return createAccountAssetsOfExchange(assetsName, exchange.getName());
                    }
                })
                .firstOrError();
    }

    /**
     * Initiate block chain assets of an account assets which belong to an exchange.
     *
     * @param accountAssets the account assets to be initialized
     * @param exchangeName  the name of the exchange
     * @return a Completable
     */
    private Single<AccountAssets> initAccountAssetsOfExchange(final AccountAssets accountAssets, final String exchangeName) {
        return exchangeRepo.getExchange(exchangeName)
                .flatMapCompletable(new Function<Exchange, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Exchange exchange) throws Exception {
                        ExchangeAssets exchangeAssets = new ExchangeAssets();
                        exchangeAssets.setAccountAssets(accountAssets);
                        exchangeAssets.setExchange(exchange);
                        AccountAssets accountAssets = exchangeAssets.getAccountAssets();
                        return blockchainAssetsRepo.initExchangeBlockchainAssets(accountAssets.getUuid(), exchange.getName());
                    }
                }).toSingleDefault(accountAssets);
    }

    /**
     * Get all exchange assets which belong to an account assets.
     *
     * @param accountAssets the account assets
     * @return a Single
     */
    @Override
    public Single<List<ExchangeAssets>> getAllExchangeAssetsOfAccount(AccountAssets accountAssets) {
        return Single.just(accountAssets)
                .flatMapObservable(new Function<AccountAssets, ObservableSource<Pair<AccountAssets, Exchange>>>() {
                    @Override
                    public ObservableSource<Pair<AccountAssets, Exchange>> apply(final AccountAssets accountAssets) throws Exception {
                        return exchangeRepo.getExchanges()
                                .flattenAsObservable(new Function<Exchange[], Iterable<Exchange>>() {
                                    @Override
                                    public Iterable<Exchange> apply(Exchange[] exchanges) throws Exception {
                                        return Arrays.asList(exchanges);
                                    }
                                })
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

    /**
     * Get all exchange assets of an exchange.
     *
     * @param exchange the exchange
     * @return a Single
     */
    @Override
    public Single<List<ExchangeAssets>> getAllExchangeAssetsOfExchange(final Exchange exchange) {
        return accountAssetsRepo.getAll()
                .flattenAsObservable(new Function<List<AccountAssets>, Iterable<AccountAssets>>() {
                    @Override
                    public Iterable<AccountAssets> apply(List<AccountAssets> accountAssets) throws Exception {
                        return accountAssets;
                    }
                }).flatMap(new Function<AccountAssets, ObservableSource<ExchangeAssets>>() {
                    @Override
                    public ObservableSource<ExchangeAssets> apply(AccountAssets accountAssets) throws Exception {
                        return getExchangeAssets(accountAssets, exchange).toObservable();
                    }
                }).toList();
    }

    /**
     * Get all exchange assets of current account assets.
     *
     * @return a Single
     */
    @Override
    public Single<List<ExchangeAssets>> getAllExchangeAssetsOfCurrentAccount() {
        return accountAssetsRepo.getCurrent()
                .flatMap(new Function<AccountAssets, SingleSource<? extends List<ExchangeAssets>>>() {
                    @Override
                    public SingleSource<? extends List<ExchangeAssets>> apply(AccountAssets accountAssets) throws Exception {
                        return getAllExchangeAssetsOfAccount(accountAssets);
                    }
                });
    }

    /**
     * Get exchange assets of an account assets and an exchange.
     *
     * @param accountAssets the account assets
     * @param exchange      the exchange
     * @return a Single
     */
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
                                                        List<BlockchainAssets> quoteBlockchainAssets) throws Exception {
                                exchangeAssets.setBlockchainAssetsList(blockchainAssets);
                                exchangeAssets.setQuoteAssetsList(quoteBlockchainAssets);
                                return exchangeAssets;
                            }
                        });
    }

    /**
     * Tell if all exchange assets have been initialized.
     *
     * @return a Single
     */
    @Override
    public Single<Boolean> isExchangeAssetsInitialized() {
        return exchangeRepo.getExchanges()
                .flattenAsObservable(new Function<Exchange[], Iterable<Exchange>>() {
                    @Override
                    public Iterable<Exchange> apply(Exchange[] exchanges) throws Exception {
                        return Arrays.asList(exchanges);
                    }
                })
                .flatMap(new Function<Exchange, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Exchange exchange) throws Exception {
                        return assetsStateRepository.getAssetsInitialized(exchange.getName()).toObservable();
                    }
                })
                .reduce(new BiFunction<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                        return aBoolean && aBoolean2;
                    }
                })
                .toSingle()
                .onErrorReturnItem(false);
    }

    @Override
    public Completable cacheExchangeAssetsToFile(final Exchange exchange) {
        return getAllExchangeAssetsOfExchange(exchange)
                .flatMapCompletable(new Function<List<ExchangeAssets>, CompletableSource>() {
                    @Override
                    public CompletableSource apply(final List<ExchangeAssets> exchangeAssets) throws Exception {
                        return Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (!exchangeAssets.isEmpty()) {
                                    FileUtils.writeFile(exchange.getName() + ASSETS_DATA_FILE, GsonHelper.prettyGson().toJson(exchangeAssets));
                                }
                            }
                        });
                    }
                });
    }

    private void sendBroadcast(String action, Bundle data) {
        Intent intent = new Intent(action);
        if (data != null) {
            intent.putExtras(data);
        }
        context.sendBroadcast(intent);
    }

    private void sendExchangeBroadcast(String exchangeName, String action, Bundle extra) {
        Bundle data = new Bundle();
        if (extra != null) {
            data.putAll(extra);
        }
        data.putString(DataBroadcasts.Extras.EXTRA_EXCHANGE_NAME, exchangeName);
        sendBroadcast(action, data);
    }
}

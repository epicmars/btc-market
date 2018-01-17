package com.fafabtc.data.data.repo.impl;

import android.content.Context;

import com.fafabtc.common.file.AndroidAssetsUtils;
import com.fafabtc.common.file.FileUtils;
import com.fafabtc.common.json.GsonHelper;
import com.fafabtc.common.utils.UUIDUtils;
import com.fafabtc.data.data.local.dao.AccountAssetsDao;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.data.repo.ExchangeAssetsRepo;
import com.fafabtc.data.data.repo.ExchangeRepo;
import com.fafabtc.data.global.AssetsState;
import com.fafabtc.data.global.AssetsStateRepository;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.data.model.vo.AccountAssetsData;
import com.fafabtc.data.model.vo.ExchangeAssets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Singleton
public class AccountAssetsRepository implements AccountAssetsRepo {

    @Inject
    AccountAssetsDao accountAssetsDao;

    @Inject
    ExchangeRepo exchangeRepo;

    @Inject
    ExchangeAssetsRepo exchangeAssetsRepo;

    @Inject
    Context context;

    @Inject
    AssetsStateRepository assetsStateRepository;

    @Inject
    public AccountAssetsRepository() {
    }

    @Override
    public Completable initAllAccountAssets() {
        // If no assets exists, create a default one, otherwise, initiate all of them.
        return Single
                .fromCallable(new Callable<List<AccountAssets>>() {
                    @Override
                    public List<AccountAssets> call() throws Exception {
                        return accountAssetsDao.findAll();
                    }
                })
                .flatMapCompletable(new Function<List<AccountAssets>, CompletableSource>() {
                    @Override
                    public CompletableSource apply(List<AccountAssets> accountAssets) throws Exception {
                        if (accountAssets.isEmpty()) {
                            return createAssets(AccountAssets.DEFAULT_NAME).toCompletable();
                        } else {
                            return Observable.fromIterable(accountAssets)
                                    .flatMapCompletable(new Function<AccountAssets, CompletableSource>() {
                                        @Override
                                        public CompletableSource apply(AccountAssets accountAssets) throws Exception {
                                            return initAssets(accountAssets);
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public Completable initAssets(AccountAssets accountAssets) {
        return Single.just(accountAssets)
                .zipWith(exchangeRepo.getExchanges(), exchangesZipFunction)
                .flattenAsObservable(new Function<List<ExchangeAssets>, Iterable<ExchangeAssets>>() {
                    @Override
                    public Iterable<ExchangeAssets> apply(List<ExchangeAssets> exchangeAssets) throws Exception {
                        return exchangeAssets;
                    }
                }).flatMapCompletable(new Function<ExchangeAssets, CompletableSource>() {
                    @Override
                    public CompletableSource apply(ExchangeAssets exchangeAssets) throws Exception {
                        return exchangeAssetsRepo.initExchangeAssets(exchangeAssets);
                    }
                });
    }

    @Override
    public Single<AccountAssets> createAssets(final String assetsName) {
        final AccountAssets assets = new AccountAssets();
        assets.setName(assetsName.trim());
        assets.setUuid(UUIDUtils.newUUID());
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                AccountAssets current = accountAssetsDao.findCurrent();
                if (null != current && !current.getUuid().equalsIgnoreCase(assets.getUuid())) {
                    current.setState(AccountAssets.State.ACTIVE);
                    accountAssetsDao.update(current);
                }
                assets.setState(AccountAssets.State.CURRENT_ACTIVE);
                accountAssetsDao.insertOne(assets);
                return assets;
            }
        }).flatMapCompletable(new Function<AccountAssets, CompletableSource>() {
            @Override
            public CompletableSource apply(AccountAssets accountAssets) throws Exception {
                return initAssets(accountAssets);
            }
        }).toSingleDefault(assets);
    }

    private BiFunction<AccountAssets, Exchange[], List<ExchangeAssets>> exchangesZipFunction = new BiFunction<AccountAssets, Exchange[], List<ExchangeAssets>>() {
        @Override
        public List<ExchangeAssets> apply(AccountAssets accountAssets, Exchange[] exchanges) throws Exception {
            if (exchanges.length == 0) return null;
            List<ExchangeAssets> exchangeAssetsList = new ArrayList<>();
            for (Exchange exchange : exchanges) {
                ExchangeAssets exchangeAssets = new ExchangeAssets();
                exchangeAssets.setAccountAssets(accountAssets);
                exchangeAssets.setExchange(exchange);
                exchangeAssetsList.add(exchangeAssets);
            }
            return exchangeAssetsList;
        }
    };

    @Override
    public Completable update(final AccountAssets... accountAssets) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                accountAssetsDao.update(accountAssets);
            }
        });
    }

    @Override
    public Completable restore(final AccountAssetsData accountAssetsData) {
        return Single.just(accountAssetsData)
                .doOnSuccess(new Consumer<AccountAssetsData>() {
                    @Override
                    public void accept(AccountAssetsData accountAssetsData) throws Exception {
                        accountAssetsDao.insertOne(accountAssetsData.getAccountAssets());
                    }
                })
                .flattenAsObservable(new Function<AccountAssetsData, Iterable<ExchangeAssets>>() {
                    @Override
                    public Iterable<ExchangeAssets> apply(AccountAssetsData accountAssetsData) throws Exception {
                        return accountAssetsData.getExchangeAssets();
                    }
                })
                .flatMapCompletable(new Function<ExchangeAssets, CompletableSource>() {
                    @Override
                    public CompletableSource apply(ExchangeAssets assets) throws Exception {
                        return exchangeAssetsRepo.restoreExchangeAssets(assets);
                    }
                }).onErrorComplete();
    }

    @Override
    public Completable init() {
        // 初始化的逻辑，如果是第一次初始化建立默认资产，否则从本地恢复数据
        return assetsStateRepository.isAssetsInitialized()
                .flatMapCompletable(new Function<Boolean, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return initAllAccountAssets();
                        } else {
                            return restoreFromFile().onErrorResumeNext(new Function<Throwable, CompletableSource>() {
                                @Override
                                public CompletableSource apply(Throwable throwable) throws Exception {
                                    return initAllAccountAssets();
                                }
                            });
                        }
                    }
                })
                .concatWith(assetsStateRepository.assetsInitialized());
    }

    private Completable restoreFromFile() {
        return Observable
                .fromCallable(new Callable<AccountAssetsData[]>() {
                    @Override
                    public AccountAssetsData[] call() throws Exception {
                        return GsonHelper.prettyGson().fromJson(FileUtils.readFile(AssetsState.ASSETS_DATA_FILE), AccountAssetsData[].class);
                    }
                })
                .onErrorReturnItem(GsonHelper.prettyGson().fromJson(AndroidAssetsUtils.readFromAssets(context.getAssets(), AssetsState.ASSETS_DATA_FILE), AccountAssetsData[].class))
                .flatMapIterable(new Function<AccountAssetsData[], Iterable<AccountAssetsData>>() {
                    @Override
                    public Iterable<AccountAssetsData> apply(AccountAssetsData[] accountAssetsData) throws Exception {
                        return Arrays.asList(accountAssetsData);
                    }
                })
                .flatMapCompletable(new Function<AccountAssetsData, CompletableSource>() {
                    @Override
                    public CompletableSource apply(AccountAssetsData accountAssetsData) throws Exception {
                        return restore(accountAssetsData);
                    }
                });
    }

    @Override
    public Single<AccountAssets> getCurrent() {
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                return accountAssetsDao.findCurrent();
            }
        });
    }

    @Override
    public Single<AccountAssets> getByUUID(final String uuid) {
        return Single.fromCallable(new Callable<AccountAssets>() {
            @Override
            public AccountAssets call() throws Exception {
                return accountAssetsDao.findByUUID(uuid);
            }
        });
    }

    @Override
    public Single<List<AccountAssets>> getAllAssets() {
        return Single.fromCallable(new Callable<List<AccountAssets>>() {
            @Override
            public List<AccountAssets> call() throws Exception {
                return accountAssetsDao.findAll();
            }
        });
    }
}

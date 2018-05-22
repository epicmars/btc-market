package com.fafabtc.data.data.repo.impl;

import android.content.Context;
import android.content.Intent;

import com.fafabtc.binance.data.repo.BinancePairRepo;
import com.fafabtc.binance.data.repo.BinanceRepo;
import com.fafabtc.binance.data.repo.BinanceTickerRepo;
import com.fafabtc.binance.model.BinancePair;
import com.fafabtc.binance.model.BinanceTicker;
import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.data.global.AssetsStateRepository;
import com.fafabtc.data.data.global.ExchangeStateRepository;
import com.fafabtc.data.data.local.dao.TickerDao;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.data.model.entity.exchange.Ticker;
import com.fafabtc.data.model.entity.mapper.TickerMapperFactory;
import com.fafabtc.gateio.data.repo.GateioRepo;
import com.fafabtc.gateio.data.repo.GateioTickerRepo;
import com.fafabtc.gateio.model.entity.GateioTicker;
import com.fafabtc.huobi.data.repo.HuobiPairRepo;
import com.fafabtc.huobi.data.repo.HuobiRepo;
import com.fafabtc.huobi.data.repo.HuobiTickerRepo;
import com.fafabtc.huobi.domain.entity.HuobiPair;
import com.fafabtc.huobi.domain.entity.HuobiTicker;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jastrelax on 2018/1/10.
 */

public class TickerRepository implements TickerRepo {

    @Inject
    TickerDao tickerDao;

    @Inject
    GateioTickerRepo gateioTickerRepo;

    @Inject
    BinanceTickerRepo binanceTickerRepo;

    @Inject
    BinancePairRepo binancePairRepo;

    @Inject
    HuobiPairRepo huobiPairRepo;

    @Inject
    HuobiTickerRepo huobiTickerRepo;

    @Inject
    Context context;

    @Inject
    ExchangeStateRepository exchangeStateRepository;

    @Inject
    public TickerRepository() {
    }

    @Override
    public Completable save(final Ticker... ticker) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                tickerDao.insert(ticker);
            }
        });
    }

    @Override
    public Completable save(final List<Ticker> tickerList) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                tickerDao.insertList(tickerList);
            }
        });
    }

    @Override
    public Single<Ticker> getLatestTickerFromCache(final String exchange, final String base, final String quote) {
        return Single.fromCallable(new Callable<Ticker>() {
            @Override
            public Ticker call() throws Exception {
                return tickerDao.getLatest(exchange, base, quote);
            }
        });
    }

    @Override
    public Single<List<Ticker>> getLatestTickerListFromCache(final String exchange) {
        return Single.fromCallable(new Callable<List<Ticker>>() {
            @Override
            public List<Ticker> call() throws Exception {
                return tickerDao.getLatest(exchange);
            }
        });
    }

    @Override
    public Single<List<Ticker>> getLatestTickers(final String exchange, final Date timestamp) {
        return exchangeStateRepository
                .getUpdateTime(exchange)
                .filter(new Predicate<Date>() {
                    @Override
                    public boolean test(Date date) throws Exception {
                        long elapsed = System.currentTimeMillis() - date.getTime();
                        return elapsed > 30 * 1000;
                    }
                })
                .flatMapSingle(new Function<Date, SingleSource<? extends List<Ticker>>>() {
                    @Override
                    public SingleSource<? extends List<Ticker>> apply(Date date) throws Exception {
                        return doGetLatestTickers(exchange, timestamp);
                    }
                });
    }

    private Single<List<Ticker>> doGetLatestTickers(final String exchange, Date timestamp) {

        Single<List<Ticker>> latestTickers = Single.error(new IllegalArgumentException("Can not find tickers of exchange: " + exchange));
        switch (exchange) {
            case GateioRepo.GATEIO_EXCHANGE:
                latestTickers = gateioTickerRepo.getLatestTickers(timestamp)
                        .compose(gateioTickerTransformer);
                break;
            case BinanceRepo.BINANCE_EXCHANGE:
                latestTickers = binanceTickerRepo.getLatestBinanceTickers(timestamp)
                        .compose(binanceTickersTransformer);
                break;
            case HuobiRepo.HUOBI_EXCHANGE:
                latestTickers = huobiTickerRepo.getLatestTickers(timestamp)
                        .compose(huobiTickersTransformer);
                break;
        }
        return latestTickers
                .compose(getLatestTickersOnSuccess(exchange))
                .doOnSubscribe(sendBroadcastActions(DataBroadcasts.Actions.ACTION_FETCH_TICKERS));
    }


    private <T> Consumer<T> sendBroadcastActions(final String action) {
        return new Consumer<T>() {
            @Override
            public void accept(T disposable) throws Exception {
                context.sendBroadcast(new Intent(action));
            }
        };
    }

    private SingleTransformer<List<Ticker>, List<Ticker>> getLatestTickersOnSuccess(final String exchange) {
        return new SingleTransformer<List<Ticker>, List<Ticker>>() {
            @Override
            public SingleSource<List<Ticker>> apply(Single<List<Ticker>> upstream) {
                return upstream
                        .doOnSuccess(new Consumer<List<Ticker>>() {
                            @Override
                            public void accept(List<Ticker> tickers) throws Exception {
                                Intent intent = new Intent(DataBroadcasts.Actions.ACTION_TICKER_UPDATED);
                                intent.putExtra(DataBroadcasts.Extras.EXTRA_EXCHANGE_NAME, exchange);
                                context.sendBroadcast(intent);
                            }
                        })
                        .flatMap(new Function<List<Ticker>, SingleSource<? extends List<Ticker>>>() {
                            @Override
                            public SingleSource<? extends List<Ticker>> apply(List<Ticker> tickers) throws Exception {
                                return exchangeStateRepository.setUpdateTime(exchange, new Date(), !tickers.isEmpty()).toSingleDefault(tickers);
                            }
                        });
            }
        };
    }

    @Override
    public Single<List<Ticker>> getAllLatestTickers() {
        Date timestamp = new Date();
        return Observable.mergeArrayDelayError(
                getLatestTickers(GateioRepo.GATEIO_EXCHANGE, timestamp)
                        .toObservable(),
                getLatestTickers(BinanceRepo.BINANCE_EXCHANGE, timestamp)
                        .toObservable(),
                getLatestTickers(HuobiRepo.HUOBI_EXCHANGE, timestamp)
                        .toObservable())
                .flatMapIterable(tickerListFlatter).toList();
    }

    private static <T> Function<List<T>, Iterable<T>> flattenList() {
        return new Function<List<T>, Iterable<T>>() {
            @Override
            public Iterable<T> apply(List<T> ts) throws Exception {
                return ts;
            }
        };
    }

    private SingleTransformer<List<HuobiTicker>, List<Ticker>> huobiTickersTransformer =
            new SingleTransformer<List<HuobiTicker>, List<Ticker>>() {
                @Override
                public SingleSource<List<Ticker>> apply(Single<List<HuobiTicker>> upstream) {
                    return upstream
                            .flattenAsObservable(TickerRepository.<HuobiTicker>flattenList())
                            .flatMapSingle(new Function<HuobiTicker, SingleSource<Ticker>>() {
                                @Override
                                public SingleSource<Ticker> apply(final HuobiTicker huobiTicker) throws Exception {
                                    return huobiPairRepo.getPairFromDb(huobiTicker.getSymbol())
                                            .map(new Function<HuobiPair, Ticker>() {
                                                @Override
                                                public Ticker apply(HuobiPair huobiPair) throws Exception {
                                                    Ticker ticker = TickerMapperFactory.HuobiTickerMapper.MAPPER.apply(huobiTicker);
                                                    ticker.setBase(huobiPair.getBase());
                                                    ticker.setQuote(huobiPair.getQuote());
                                                    return ticker;
                                                }
                                            });
                                }
                            })
                            .toList()
                            .doOnSuccess(saveTickers);
                }
            };

    private SingleTransformer<List<GateioTicker>, List<Ticker>> gateioTickerTransformer =
            new SingleTransformer<List<GateioTicker>, List<Ticker>>() {
                @Override
                public SingleSource<List<Ticker>> apply(Single<List<GateioTicker>> upstream) {
                    return upstream
                            .flattenAsObservable(new Function<List<GateioTicker>, Iterable<GateioTicker>>() {
                                @Override
                                public Iterable<GateioTicker> apply(List<GateioTicker> gateioTickers) throws Exception {
                                    return gateioTickers;
                                }
                            })
                            .map(TickerMapperFactory.GateioTickerMapper.MAPPER)
                            .toList()
                            .doOnSuccess(saveTickers);
                }
            };


    private SingleTransformer<List<BinanceTicker>, List<Ticker>> binanceTickersTransformer =
            new SingleTransformer<List<BinanceTicker>, List<Ticker>>() {
                @Override
                public SingleSource<List<Ticker>> apply(Single<List<BinanceTicker>> upstream) {
                    return upstream
                            .flattenAsObservable(new Function<List<BinanceTicker>, Iterable<BinanceTicker>>() {
                                @Override
                                public Iterable<BinanceTicker> apply(List<BinanceTicker> tickerList) throws Exception {
                                    return tickerList;
                                }
                            })
                            .flatMapSingle(new Function<BinanceTicker, SingleSource<Ticker>>() {
                                @Override
                                public SingleSource<Ticker> apply(BinanceTicker binanceTicker) throws Exception {
                                    final Ticker ticker = TickerMapperFactory.BinanceTickerMapper.MAPPER.apply(binanceTicker);
                                    return binancePairRepo.getBinancePair(binanceTicker.getSymbol())
                                            .map(new Function<BinancePair, Ticker>() {
                                                @Override
                                                public Ticker apply(BinancePair binancePair) throws Exception {
                                                    ticker.setBase(binancePair.getBase());
                                                    ticker.setQuote(binancePair.getQuote());
                                                    return ticker;
                                                }
                                            });
                                }
                            })
                            .toList()
                            .doOnSuccess(saveTickers);
                }
            };

    private Consumer<List<Ticker>> saveTickers = new Consumer<List<Ticker>>() {
        @Override
        public void accept(List<Ticker> tickerList) throws Exception {
            tickerDao.insertList(tickerList);
        }
    };

    private Function<List<Ticker>, Iterable<Ticker>> tickerListFlatter = new Function<List<Ticker>, Iterable<Ticker>>() {
        @Override
        public Iterable<Ticker> apply(List<Ticker> tickerList) throws Exception {
            return tickerList;
        }
    };

}

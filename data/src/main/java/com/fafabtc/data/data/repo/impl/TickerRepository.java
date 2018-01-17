package com.fafabtc.data.data.repo.impl;

import com.fafabtc.binance.data.repo.BinancePairRepo;
import com.fafabtc.binance.data.repo.BinanceRepo;
import com.fafabtc.binance.data.repo.BinanceTickerRepo;
import com.fafabtc.binance.model.BinancePair;
import com.fafabtc.binance.model.BinanceTicker;
import com.fafabtc.data.data.local.dao.TickerDao;
import com.fafabtc.data.data.repo.TickerRepo;
import com.fafabtc.data.model.entity.exchange.Ticker;
import com.fafabtc.data.model.entity.mapper.TickerMapperFactory;
import com.fafabtc.gateio.data.repo.GateioRepo;
import com.fafabtc.gateio.data.repo.GateioTickerRepo;
import com.fafabtc.gateio.model.entity.GateioTicker;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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

    public Single<List<Ticker>> getLatestTickers(final String exchange, Date timestamp) {
        Single<List<Ticker>> gateioTickers = gateioTickerRepo.getLatestTickers(timestamp)
                .singleOrError()
                .compose(gateioTickerTransformer);
        Single<List<Ticker>> binanceTickers = binanceTickerRepo.getLatestBinanceTickers(timestamp)
                .compose(binanceTickersTransformer);
        switch (exchange) {
            case GateioRepo.GATEIO_EXCHANGE:
                return gateioTickers;
            case BinanceRepo.BINANCE_EXCHANGE:
                return binanceTickers;
        }
        return Single.error(new IllegalArgumentException("Can not find tickers of exchange: " + exchange));
    }

    private SingleTransformer<List<GateioTicker>, List<Ticker>> gateioTickerTransformer =
            new SingleTransformer<List<GateioTicker>, List<Ticker>>() {
                @Override
                public SingleSource<List<Ticker>> apply(Single<List<GateioTicker>> upstream) {
                    return upstream
                            .toFlowable()
                            .flatMapIterable(new Function<List<GateioTicker>, Iterable<GateioTicker>>() {
                                @Override
                                public Iterable<GateioTicker> apply(List<GateioTicker> gateioTickers) throws Exception {
                                    return gateioTickers;
                                }
                            })
                            .map(new Function<GateioTicker, Ticker>() {
                                @Override
                                public Ticker apply(GateioTicker gateioTicker) throws Exception {
                                    return TickerMapperFactory.mapFrom(gateioTicker);
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
                            .flatMap(new Function<BinanceTicker, ObservableSource<Ticker>>() {
                                @Override
                                public ObservableSource<Ticker> apply(BinanceTicker binanceTicker) throws Exception {
                                    final Ticker ticker = TickerMapperFactory.mapFrom(binanceTicker);
                                    return binancePairRepo.getBinancePair(binanceTicker.getSymbol())
                                            .toObservable()
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

    Function<List<Ticker>, Iterable<Ticker>> tickerListFlatter = new Function<List<Ticker>, Iterable<Ticker>>() {
        @Override
        public Iterable<Ticker> apply(List<Ticker> tickerList) throws Exception {
            return tickerList;
        }
    };

    @Override
    public Single<List<Ticker>> getAllLatestTickers() {
        Date timestamp = new Date();
        return Observable.mergeArrayDelayError(getLatestTickers(GateioRepo.GATEIO_EXCHANGE, timestamp).toObservable(),
                getLatestTickers(BinanceRepo.BINANCE_EXCHANGE, timestamp).toObservable())
                .flatMapIterable(tickerListFlatter).toList();
    }
}

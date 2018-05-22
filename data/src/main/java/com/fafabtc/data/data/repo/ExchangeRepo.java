package com.fafabtc.data.data.repo;

import com.fafabtc.binance.data.repo.BinanceRepo;
import com.fafabtc.data.model.entity.exchange.Exchange;
import com.fafabtc.gateio.data.repo.GateioRepo;
import com.fafabtc.huobi.data.repo.HuobiRepo;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface ExchangeRepo {

    String[] EXCHANGES = {
            HuobiRepo.HUOBI_EXCHANGE,
            GateioRepo.GATEIO_EXCHANGE,
            BinanceRepo.BINANCE_EXCHANGE
    };

    Completable initAllExchanges();

    Completable initGateio();

    Completable initBinance();

    Completable initHuobi();

    Completable initExchange(String exchange);

    Completable saveExchange(String exchangeName);

    Completable saveExchange(Exchange exchange);

    Single<Exchange[]> getExchanges();

    Single<Exchange> getExchange(String name);
}

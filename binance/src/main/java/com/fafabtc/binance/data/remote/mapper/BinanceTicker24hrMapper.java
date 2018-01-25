package com.fafabtc.binance.data.remote.mapper;

import com.fafabtc.binance.data.remote.dto.BinanceTicker24hr;
import com.fafabtc.binance.model.BinanceTicker;
import com.fafabtc.domain.data.remote.Mapper;

import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/13.
 */

public enum BinanceTicker24hrMapper implements Mapper<BinanceTicker24hr, BinanceTicker>, Function<BinanceTicker24hr, BinanceTicker> {

    MAPPER;

    @Override
    public BinanceTicker apply(BinanceTicker24hr source) {
        if (source == null) return null;
        BinanceTicker ticker = new BinanceTicker();
        ticker.setSymbol(source.getSymbol());
        ticker.setLastPrice(Double.valueOf(source.getLastPrice()));
        ticker.setPriceChangePercent(Double.valueOf(source.getPriceChangePercent()));
        ticker.setAskPrice(Double.valueOf(source.getAskPrice()));
        ticker.setBidPrice(Double.valueOf(source.getBidPrice()));
        ticker.setVolume(Double.valueOf(source.getVolume()));
        ticker.setQuoteVolume(Double.valueOf(source.getQuoteVolume()));
        ticker.setHighPrice(Double.valueOf(source.getHighPrice()));
        ticker.setLowPrice(Double.valueOf(source.getLowPrice()));
        return ticker;
    }

}

package com.fafabtc.data.model.entity.mapper;

import com.fafabtc.binance.model.BinanceTicker;
import com.fafabtc.data.model.entity.exchange.Ticker;
import com.fafabtc.domain.data.remote.Mapper;
import com.fafabtc.gateio.model.entity.GateioTicker;

/**
 * Created by jastrelax on 2018/1/10.
 */

public class TickerMapperFactory {

    private static final GateioTickerMapper gateioTickerMapper = new GateioTickerMapper();
    private static final BinanceTickerMapper binanceTickerMapper = new BinanceTickerMapper();

    public static <T> Ticker mapFrom(T sourceTicker) {
        if (sourceTicker instanceof GateioTicker) {
            return gateioTickerMapper.from((GateioTicker) sourceTicker);
        } else if (sourceTicker instanceof BinanceTicker) {
            return binanceTickerMapper.from((BinanceTicker) sourceTicker);
        }
        return null;
    }

    public static class GateioTickerMapper implements Mapper<GateioTicker, Ticker> {
        @Override
        public Ticker from(GateioTicker source) {
            if (source == null) return null;
            Ticker ticker = new Ticker();
            ticker.setTimestamp(source.getTimestamp());
            ticker.setExchange(source.getExchange());
            ticker.setAsk(source.getLowestAsk());
            ticker.setBid(source.getHighestBid());
            ticker.setPair(source.getPair());
            ticker.setLast(source.getLast());
            ticker.setPercentChange(source.getPercentChange());
            // base volume and quote volume has reversed.
            ticker.setBaseVolume(source.getQuoteVolume());
            ticker.setQuoteVolume(source.getBaseVolume());
            ticker.setHigh24hr(source.getHigh24hr());
            ticker.setLow24hr(source.getLow24hr());
            String[] pairs = source.getPair().split("_");
            if (pairs != null && pairs.length == 2) {
                ticker.setBase(pairs[0]);
                ticker.setQuote(pairs[1]);
            }
            return ticker;
        }
    }

    public static class BinanceTickerMapper implements Mapper<BinanceTicker, Ticker> {
        @Override
        public Ticker from(BinanceTicker source) {
            if (source == null) return null;
            Ticker ticker = new Ticker();
            ticker.setTimestamp(source.getTimestamp());
            ticker.setExchange(source.getExchange());
            ticker.setAsk(source.getAskPrice());
            ticker.setBid(source.getBidPrice());
            ticker.setPair(source.getSymbol());
            ticker.setLast(source.getLastPrice());
            ticker.setPercentChange(source.getPriceChangePercent());
            ticker.setBaseVolume(source.getVolume());
            ticker.setQuoteVolume(source.getQuoteVolume());
            ticker.setHigh24hr(source.getHighPrice());
            ticker.setLow24hr(source.getLowPrice());
            return ticker;
        }
    }
}

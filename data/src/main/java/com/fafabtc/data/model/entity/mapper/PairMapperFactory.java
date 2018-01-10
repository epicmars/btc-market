package com.fafabtc.data.model.entity.mapper;

import com.fafabtc.binance.model.BinancePair;
import com.fafabtc.data.model.entity.exchange.Pair;
import com.fafabtc.domain.data.remote.Mapper;
import com.fafabtc.gateio.model.entity.GateioPair;

/**
 * Created by jastrelax on 2018/1/10.
 */

public class PairMapperFactory {

    public static GateioPairMapper gateioPairMapper = new GateioPairMapper();
    public static BinancePairMapper binancePairMapper = new BinancePairMapper();

    public static class GateioPairMapper implements Mapper<GateioPair, Pair> {

        @Override
        public Pair from(GateioPair source) {
            if (source == null) return null;
            Pair pair = new Pair();
            pair.setExchange(source.getExchange());
            pair.setBase(source.getBase());
            pair.setPair(source.getName());
            pair.setQuote(source.getQuote());
            return pair;
        }
    }

    public static class BinancePairMapper implements Mapper<BinancePair, Pair> {

        @Override
        public Pair from(BinancePair source) {
            if (source == null) return null;
            Pair pair = new Pair();
            pair.setExchange(source.getExchange());
            pair.setBase(source.getBase());
            pair.setQuote(source.getQuote());
            pair.setPair(source.getSymbol());
            return pair;
        }
    }


}

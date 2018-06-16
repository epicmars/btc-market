package com.fafabtc.data.model.entity.mapper;

import com.fafabtc.binance.model.BinancePair;
import com.fafabtc.data.model.entity.exchange.Pair;
import com.fafabtc.base.data.remote.Mapper;
import com.fafabtc.base.data.remote.MapperFactory;
import com.fafabtc.gateio.model.entity.GateioPair;
import com.fafabtc.huobi.domain.entity.HuobiPair;

/**
 * Created by jastrelax on 2018/1/10.
 */

public enum PairMapperFactory implements MapperFactory{

    FACTORY;

    @Override
    public Mapper create(Object object) {
        if (object instanceof GateioPair) {
            return GateioPairMapper.MAPPER;
        } else if (object instanceof BinancePair) {
            return BinancePairMapper.MAPPER;
        } else if (object instanceof HuobiPair) {
            return HuobiPairMapper.MAPPER;
        }
        return null;
    }

    public enum GateioPairMapper implements Mapper<GateioPair, Pair> {

        MAPPER;

        @Override
        public Pair apply(GateioPair source) {
            if (source == null) return null;
            Pair pair = new Pair();
            pair.setExchange(source.getExchange());
            pair.setBase(source.getBase());
            pair.setPair(source.getName());
            pair.setQuote(source.getQuote());
            return pair;
        }
    }

    public enum BinancePairMapper implements Mapper<BinancePair, Pair> {

        MAPPER;

        @Override
        public Pair apply(BinancePair source) {
            if (source == null) return null;
            Pair pair = new Pair();
            pair.setExchange(source.getExchange());
            pair.setBase(source.getBase());
            pair.setQuote(source.getQuote());
            pair.setPair(source.getSymbol());
            return pair;
        }
    }

    public enum HuobiPairMapper implements Mapper<HuobiPair, Pair> {
        MAPPER;
        @Override
        public Pair apply(HuobiPair source) {
            if (source == null) return null;
            Pair pair = new Pair();
            pair.setExchange(source.getExchange());
            pair.setPair(source.getSymbol());
            pair.setBase(source.getBase());
            pair.setQuote(source.getQuote());
            return pair;
        }
    }


}

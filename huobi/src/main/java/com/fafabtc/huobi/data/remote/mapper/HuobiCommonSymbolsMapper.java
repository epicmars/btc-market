package com.fafabtc.huobi.data.remote.mapper;

import com.fafabtc.domain.data.remote.Mapper;
import com.fafabtc.huobi.data.remote.dto.HuobiCommonSymbols;
import com.fafabtc.huobi.domain.entity.HuobiPair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2018/1/25.
 */

public enum HuobiCommonSymbolsMapper implements Mapper<HuobiCommonSymbols, List<HuobiPair>> {

    MAPPER;

    @Override
    public List<HuobiPair> apply(HuobiCommonSymbols source) {
        if (source == null) return null;
        List<HuobiPair> huobiPairs = new ArrayList<>();
        for (HuobiCommonSymbols.SymbolBean symbolBean : source.getData()) {
            HuobiPair pair = new HuobiPair();
            pair.setBase(symbolBean.getBasecurrency());
            pair.setQuote(symbolBean.getQuotecurrency());
            pair.setSymbol(symbolBean.getBasecurrency() + symbolBean.getQuotecurrency());
            huobiPairs.add(pair);
        }
        return huobiPairs;
    }
}

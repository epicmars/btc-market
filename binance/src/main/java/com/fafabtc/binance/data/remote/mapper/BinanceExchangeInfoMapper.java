package com.fafabtc.binance.data.remote.mapper;

import com.fafabtc.binance.data.remote.dto.BinanceExchangeInfo;
import com.fafabtc.binance.model.BinancePair;
import com.fafabtc.base.data.remote.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastrelax on 2018/1/13.
 */

public enum  BinanceExchangeInfoMapper implements Mapper<BinanceExchangeInfo, List<BinancePair>> {

    MAPPER;

    @Override
    public List<BinancePair> apply(BinanceExchangeInfo source) {
        if (source == null) return null;
        List<BinanceExchangeInfo.SymbolsBean> symbolsBeanList = source.getSymbols();
        if (symbolsBeanList == null || symbolsBeanList.isEmpty()) return null;

        List<BinancePair> binancePairs = new ArrayList<>();
        for (BinanceExchangeInfo.SymbolsBean symbolsBean : symbolsBeanList) {
            BinancePair pair = new BinancePair();
            pair.setSymbol(symbolsBean.getSymbol());
            pair.setBase(symbolsBean.getBaseAsset());
            pair.setQuote(symbolsBean.getQuoteAsset());
            binancePairs.add(pair);
        }
        return binancePairs;
    }
}

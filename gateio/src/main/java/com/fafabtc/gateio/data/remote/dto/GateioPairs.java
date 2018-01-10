package com.fafabtc.gateio.data.remote.dto;

import com.fafabtc.gateio.model.entity.GateioPair;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易对。
 * <pre>
 *    eth_btc: 以太币对比特币交易
 *    etc_btc: 以太经典对比特币
 *    etc_eth: 以太经典对以太币
 *    xrp_btc: 瑞波币对比特币
 *    zec_btc: ZCash对比特币
 *    ....
 * </pre>
 * Created by jastrelax on 2018/1/7.
 */

public class GateioPairs {

    private String[] pairs;

    public String[] getPairs() {
        return pairs;
    }

    public void setPairs(String[] pairs) {
        this.pairs = pairs;
    }

    public List<GateioPair> toGateioPairList() {
        List<GateioPair> list = new ArrayList<>();
        if (pairs == null) return list;
        for (String p : pairs) {
            GateioPair pair = new GateioPair();
            pair.setName(p);
            String[] pairStrings = p.split("_");
            if (pairStrings != null && pairStrings.length == 2) {
                pair.setBase(pairStrings[0]);
                pair.setQuote(pairStrings[1]);
            }
            list.add(pair);
        }
        return list;
    }
}

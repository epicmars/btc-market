package com.fafabtc.huobi.data.remote.mapper;

import com.fafabtc.base.data.remote.Mapper;
import com.fafabtc.huobi.data.remote.dto.HuobiMarketDetailMerged;
import com.fafabtc.huobi.domain.entity.HuobiTicker;

/**
 * Created by jastrelax on 2018/1/25.
 */

public enum  HuobiMarketDetailMergedMapper implements Mapper<HuobiMarketDetailMerged, HuobiTicker> {

    MAPPER;

    @Override
    public HuobiTicker apply(HuobiMarketDetailMerged source) {
        if (source == null) return null;
        HuobiTicker ticker = new HuobiTicker();
        HuobiMarketDetailMerged.TickBean tickBean= source.getTick();
        ticker.setAsk(tickBean.getAsk().get(0));
        ticker.setAmount(tickBean.getAmount());
        ticker.setBid(tickBean.getBid().get(0));
        ticker.setClose(tickBean.getClose());
        ticker.setCount(tickBean.getCount());
        ticker.setHigh(tickBean.getHigh());
        ticker.setKlineId(tickBean.getId());
        ticker.setLow(tickBean.getLow());
        ticker.setOpen(tickBean.getOpen());
        ticker.setVol(tickBean.getVol());
        ticker.setVersion(tickBean.getVersion());
        return ticker;
    }
}

package com.fafabtc.data.model.vo;

import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class ExchangeAssets {

    private Portfolio portfolio;

    private Exchange exchange;

    private List<BlockchainAssets> quoteAssetsList;

    private List<BlockchainAssets> blockchainAssetsList;

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        if (null == portfolio) return;
        this.portfolio = portfolio;
        if (blockchainAssetsList != null) {
            for (BlockchainAssets assets : blockchainAssetsList)
                assets.setAssetsUuid(portfolio.getUuid());
        }
        if (quoteAssetsList != null) {
            for (BlockchainAssets assets : quoteAssetsList)
                assets.setAssetsUuid(portfolio.getUuid());
        }
    }

    public List<BlockchainAssets> getBlockchainAssetsList() {
        return blockchainAssetsList;
    }

    public void setBlockchainAssetsList(List<BlockchainAssets> blockchainAssetsList) {
        this.blockchainAssetsList = blockchainAssetsList;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public List<BlockchainAssets> getQuoteAssetsList() {
        return quoteAssetsList;
    }

    public void setQuoteAssetsList(List<BlockchainAssets> quoteAssetsList) {
        this.quoteAssetsList = quoteAssetsList;
    }
}

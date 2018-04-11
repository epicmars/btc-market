package com.fafabtc.data.model.vo;

import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/8.
 */

public class ExchangeAssets {

    private AccountAssets accountAssets;

    private Exchange exchange;

    private List<BlockchainAssets> quoteAssetsList;

    private List<BlockchainAssets> blockchainAssetsList;

    public AccountAssets getAccountAssets() {
        return accountAssets;
    }

    public void setAccountAssets(AccountAssets accountAssets) {
        if (null == accountAssets) return;
        this.accountAssets = accountAssets;
        if (blockchainAssetsList != null) {
            for (BlockchainAssets assets : blockchainAssetsList)
                assets.setAssetsUUID(accountAssets.getUuid());
        }
        if (quoteAssetsList != null) {
            for (BlockchainAssets assets : quoteAssetsList)
                assets.setAssetsUUID(accountAssets.getUuid());
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

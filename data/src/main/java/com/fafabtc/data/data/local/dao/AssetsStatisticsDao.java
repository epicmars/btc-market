package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.fafabtc.data.model.vo.AssetsStatistics;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/11.
 */
@Dao
public interface AssetsStatisticsDao {

    @Query("with \n" +
            "     all_quotes(q) as (select distinct quote from ticker),\n" +
            "     quote_ticker as (select * from ticker where base in all_quotes and quote = 'usdt' COLLATE NOCASE),\n" +
            "     assets as (select blockchain_assets.assets_uuid,\n" +
            "     blockchain_assets.exchange,\n" +
            "     blockchain_assets.name,\n" +
            "     blockchain_assets.available,\n" +
            "     blockchain_assets.locked,\n" +
            "     ticker.last,\n" +
            "     ticker.\"quote\",\n" +
            "     ticker.base_volume\n" +
            "     from blockchain_assets left outer join ticker\n" +
            "     on blockchain_assets.name = ticker.base\n" +
            "     and blockchain_assets.exchange = ticker.exchange\n" +
            "     where blockchain_assets.assets_uuid = :assetsUUID),\n" +
            "     statistics as (select assets.*, quote_ticker.last as quote_last,\n" +
            "        case\n" +
            "            when assets.quote = 'usdt' COLLATE NOCASE then assets.last\n" +
            "            else assets.last * quote_ticker.last\n" +
            "        end\n" +
            "        as usdt_last\n" +
            "     from assets left outer join quote_ticker on assets.quote = quote_ticker.base)\n" +
            "     select *, avg(statistics.usdt_last) as usdt_avg, max(base_volume) from statistics group by statistics.name, statistics.exchange;")
    List<AssetsStatistics> findAssetsStatistics(String assetsUUID);
}

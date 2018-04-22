package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import com.fafabtc.data.model.vo.AssetsStatistics;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/11.
 */
@Dao
public interface AssetsStatisticsDao {

    @Query("select *, avg(statistics.usdt_last) as usdt_avg, max(base_volume) as max_base_volume \n" +
           "from (select assets.*, quote_ticker.last as quote_last, \n" +
           "          case\n" +
           "              when assets.quote = 'usdt' COLLATE NOCASE \n" +
           "                  then assets.last \n" +
           "              else assets.last * quote_ticker.last \n" +
           "          end \n" +
           "          as usdt_last \n" +
           "      from (select blockchain_assets.assets_uuid, \n" +
           "            blockchain_assets.exchange, \n" +
           "            blockchain_assets.name, \n" +
           "            blockchain_assets.available, \n" +
           "            blockchain_assets.locked, \n" +
           "            ticker.last, \n" +
           "            ticker.quote, \n" +
           "            ticker.base_volume \n" +
           "            from blockchain_assets left outer join ticker\n" +
           "            on blockchain_assets.name = ticker.base \n" +
           "            and blockchain_assets.exchange = ticker.exchange\n" +
           "      where blockchain_assets.assets_uuid = :assetsUUID) as assets \n" +
           "      left outer join \n" +
           "          (select * from ticker where base in \n" +
           "              (select distinct quote from ticker) \n" +
           "           and quote = 'usdt' COLLATE NOCASE) as quote_ticker \n" +
           "      on assets.quote = quote_ticker.base) as statistics \n" +
           "group by statistics.name, statistics.exchange;")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    List<AssetsStatistics> findAssetsStatistics(String assetsUUID);
}

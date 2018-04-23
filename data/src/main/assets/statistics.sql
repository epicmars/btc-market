with
     all_quotes(q) as (select distinct quote from ticker),
     quote_ticker as (select * from ticker where base in all_quotes and quote = 'usdt' COLLATE NOCASE),
     assets as (select blockchain_assets.assets_uuid,
     blockchain_assets.exchange,
     blockchain_assets.name,
     blockchain_assets.available,
     blockchain_assets.locked,
     ticker.last,
     ticker."quote",
     ticker.base_volume
     from blockchain_assets left outer join ticker
     on blockchain_assets.name = ticker.base
     and blockchain_assets.exchange = ticker.exchange),
     statistics as (select assets.*, quote_ticker.last as quote_last,
        case
            when assets.quote = 'usdt' COLLATE NOCASE then assets.last
            else assets.last * quote_ticker.last
        end
        as usdt_last
     from assets left outer join quote_ticker on assets.quote = quote_ticker.base)
     select *, avg(statistics.usdt_last) as usdt_avg, max(base_volume) from statistics group by statistics.name, statistics.exchange;


select *, avg(statistics.usdt_last) as usdt_avg, max(base_volume) as max_base_volume
from (select assets.*, quote_ticker.last as quote_last,
          case
              when assets.quote = 'usdt' COLLATE NOCASE
                  then assets.last
              else assets.last * quote_ticker.last
          end
          as usdt_last
      from (select blockchain_assets.assets_uuid,
          blockchain_assets.exchange,
          blockchain_assets.name,
          blockchain_assets.available,
          blockchain_assets.locked,
          ticker.last,
          ticker.quote,
          ticker.base_volume
          from blockchain_assets left outer join ticker
          on blockchain_assets.name = ticker.base
          and blockchain_assets.exchange = ticker.exchange
      where blockchain_assets.assets_uuid = '36b51249-9324-492d-a8e2-398db9acdf0e') as assets
      left outer join
          (select * from ticker where base in
              (select distinct quote from ticker)
           and quote = 'usdt' COLLATE NOCASE) as quote_ticker
      on assets.quote = quote_ticker.base) as statistics
group by statistics.name, statistics.exchange;
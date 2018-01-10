with max_timestamp(m) as (select max(timestamp) from ticker),
     all_quotes(q) as (select distinct quote from ticker),
     quote_ticker as (select * from ticker where timestamp in max_timestamp and base in all_quotes and quote = 'usdt' COLLATE NOCASE),
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
     and blockchain_assets.exchange = ticker.exchange
     where ticker.timestamp in max_timestamp),
     statistics as (select assets.*, quote_ticker.last as quote_last,
        case
            when assets.quote = 'usdt' COLLATE NOCASE then assets.last
            else assets.last * quote_ticker.last
        end
        as usdt_last
     from assets left outer join quote_ticker on assets.quote = quote_ticker.base)
     select *, avg(statistics.usdt_last) as usdt_avg, max(base_volume) from statistics group by statistics.name;
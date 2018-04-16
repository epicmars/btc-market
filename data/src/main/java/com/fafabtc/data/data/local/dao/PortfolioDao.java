package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.fafabtc.common.utils.UUIDUtils;
import com.fafabtc.data.model.entity.exchange.Portfolio;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Dao
public abstract class PortfolioDao {

    @Insert
    public abstract Long[] insert(Portfolio... portfolios);

    @Update
    public abstract void update(Portfolio... portfolios);

    @Delete
    public abstract void deleteMany(Portfolio... portfolios);

    @Query("select * from portfolio where state = :state")
    public abstract List<Portfolio> findByState(String state);

    @Query("select * from portfolio where state = 'CURRENT_ACTIVE' limit 1")
    public abstract Portfolio findCurrent();

    @Query("select * from portfolio where uuid = :uuid limit 1")
    public abstract Portfolio findByUUID(String uuid);

    @Query("select * from portfolio where name = :name limit 1")
    public abstract Portfolio findByName(String name);

    @Query("select * from portfolio")
    public abstract List<Portfolio> findAll();

    @Transaction
    public void delete(Portfolio... portfolios) {
        deleteMany(portfolios);
        for (Portfolio portfolio : portfolios) {
            if (portfolio.getState() == Portfolio.State.CURRENT_ACTIVE) {
                List<Portfolio> portfolioList = findAll();
                if (portfolioList.isEmpty()) {
                    return;
                } else {
                    Portfolio firstPortfolio = portfolioList.get(0);
                    firstPortfolio.setState(Portfolio.State.CURRENT_ACTIVE);
                    update(firstPortfolio);
                }
            }
        }
    }

    @Transaction
    public Portfolio create(String assetsName) {
        if (assetsName == null)
            throw new NullPointerException("The new account assets to be created is null.");

        Portfolio current = findCurrent();
        Portfolio cachedNewAssets = findByName(assetsName);
        if (current != null) {
            current.setState(Portfolio.State.ACTIVE);
            update(current);
        }

        if (cachedNewAssets == null) {
            final Portfolio newAssets = new Portfolio();
            newAssets.setName(assetsName.trim());
            newAssets.setUuid(UUIDUtils.newUUID());
            newAssets.setState(Portfolio.State.CURRENT_ACTIVE);
            insert(newAssets);
            cachedNewAssets = newAssets;
        } else if (cachedNewAssets.getState() != Portfolio.State.CURRENT_ACTIVE){
            cachedNewAssets.setState(Portfolio.State.CURRENT_ACTIVE);
            update(cachedNewAssets);
        }
        return cachedNewAssets;
    }
}

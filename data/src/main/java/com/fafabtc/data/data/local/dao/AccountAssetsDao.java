package com.fafabtc.data.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.fafabtc.common.utils.UUIDUtils;
import com.fafabtc.data.model.entity.exchange.AccountAssets;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Dao
public abstract class AccountAssetsDao {

    @Insert
    public abstract Long[] insert(AccountAssets... accountAssets);

    @Update
    public abstract void update(AccountAssets... accountAssets);

    @Delete
    public abstract void deleteMany(AccountAssets... accountAssets);

    @Query("select * from account_assets where state = :state")
    public abstract List<AccountAssets> findByState(String state);

    @Query("select * from account_assets where state = 'CURRENT_ACTIVE' limit 1")
    public abstract AccountAssets findCurrent();

    @Query("select * from account_assets where uuid = :uuid limit 1")
    public abstract AccountAssets findByUUID(String uuid);

    @Query("select * from account_assets where name = :name limit 1")
    public abstract AccountAssets findByName(String name);

    @Query("select * from account_assets")
    public abstract List<AccountAssets> findAll();

    @Transaction
    public void delete(AccountAssets... accountAssets) {
        deleteMany(accountAssets);
        for (AccountAssets assets : accountAssets) {
            if (assets.getState() == AccountAssets.State.CURRENT_ACTIVE) {
                List<AccountAssets> assetsList = findAll();
                if (assetsList.isEmpty()) {
                    return;
                } else {
                    AccountAssets firstAssets = assetsList.get(0);
                    firstAssets.setState(AccountAssets.State.CURRENT_ACTIVE);
                    update(firstAssets);
                }
            }
        }
    }

    @Transaction
    public AccountAssets create(String assetsName) {
        if (assetsName == null)
            throw new NullPointerException("The new account assets to be created is null.");

        AccountAssets current = findCurrent();
        AccountAssets cachedNewAssets = findByName(assetsName);
        if (current != null) {
            current.setState(AccountAssets.State.ACTIVE);
            update(current);
        }

        if (cachedNewAssets == null) {
            final AccountAssets newAssets = new AccountAssets();
            newAssets.setName(assetsName.trim());
            newAssets.setUuid(UUIDUtils.newUUID());
            newAssets.setState(AccountAssets.State.CURRENT_ACTIVE);
            insert(newAssets);
            cachedNewAssets = newAssets;
        } else if (cachedNewAssets.getState() != AccountAssets.State.CURRENT_ACTIVE){
            cachedNewAssets.setState(AccountAssets.State.CURRENT_ACTIVE);
            update(cachedNewAssets);
        }
        return cachedNewAssets;
    }
}

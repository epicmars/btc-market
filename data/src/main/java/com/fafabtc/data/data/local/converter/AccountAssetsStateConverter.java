package com.fafabtc.data.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.fafabtc.data.model.entity.exchange.AccountAssets;

/**
 * Created by jastrelax on 2018/1/9.
 */

public class AccountAssetsStateConverter {

    @TypeConverter
    public String fromState(AccountAssets.State state) {
        if (state == null) return null;
        return state.name();
    }

    @TypeConverter
    public AccountAssets.State toState(String stateStr) {
        for (AccountAssets.State s : AccountAssets.State.values()) {
            if (s.name().equalsIgnoreCase(stateStr))
                return s;
        }
        return null;
    }
}

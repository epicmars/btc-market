package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.entity.exchange.Portfolio;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/8.
 */

public interface PortfolioRepo {

    Completable save(Portfolio... portfolios);

    Completable delete(Portfolio portfolio);

    Completable update(Portfolio... portfolios);

    Single<Portfolio> getCurrent();

    Single<Portfolio> getByUUID(String uuid);

    Single<Portfolio> getByName(String name);

    Single<Boolean> isCreated(String name);

    Single<List<Portfolio>> getAll();

    Single<Portfolio> create(String assetsName);
}

package com.fafabtc.data.data.repo.impl;

import com.fafabtc.data.data.local.dao.AssetsStatisticsDao;
import com.fafabtc.data.data.repo.AssetsStatisticsRepo;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.model.vo.AssetsStatistics;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by jastrelax on 2018/1/11.
 */

public class AssetsStatisticsRepository implements AssetsStatisticsRepo {

    @Inject
    AssetsStatisticsDao dao;

    @Inject
    BlockchainAssetsRepo blockchainAssetsRepo;

    @Inject
    public AssetsStatisticsRepository() {
    }

    @Override
    public Single<List<AssetsStatistics>> getAccountStatistics(final String assetsUUID) {
        return Single.fromCallable(new Callable<List<AssetsStatistics>>() {
            @Override
            public List<AssetsStatistics> call() throws Exception {
                return dao.findAssetsStatistics(assetsUUID);
            }
        });
    }

    @Override
    public Single<Double> getAccountTotalVolume(String assetsUUID) {
        Single<Double> availableUSDT = blockchainAssetsRepo.getUsdtBalanceFromAccount(assetsUUID);
        return getAccountStatistics(assetsUUID)
                .flattenAsObservable(new Function<List<AssetsStatistics>, Iterable<AssetsStatistics>>() {
                    @Override
                    public Iterable<AssetsStatistics> apply(List<AssetsStatistics> assetsStatistics) throws Exception {
                        return assetsStatistics;
                    }
                })
                .map(new Function<AssetsStatistics, Double>() {
                    @Override
                    public Double apply(AssetsStatistics assetsStatistics) throws Exception {
                        return assetsStatistics.getUsdtLast() * assetsStatistics.getAvailable();
                    }
                })
                .reduce(0.0, new BiFunction<Double, Double, Double>() {
                    @Override
                    public Double apply(Double aDouble, Double aDouble2) throws Exception {
                        return aDouble + aDouble2;
                    }
                })
                .zipWith(availableUSDT, new BiFunction<Double, Double, Double>() {
                    @Override
                    public Double apply(Double aDouble, Double aDouble2) throws Exception {
                        return aDouble + aDouble2;
                    }
                });
    }
}

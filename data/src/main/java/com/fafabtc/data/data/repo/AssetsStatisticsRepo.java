package com.fafabtc.data.data.repo;

import com.fafabtc.data.model.vo.AssetsStatistics;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by jastrelax on 2018/1/11.
 */

public interface AssetsStatisticsRepo {

    Single<List<AssetsStatistics>> getAccountStatistics(String assetsUUID);

    Single<Double> getAccountTotalVolume(String assetsUUID);
}

package com.fafabtc.data.model.vo;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/14.
 */

public class AssetsStatisticsHolder {

    private AssetsStatisticsHeader assetsStatisticsHeader;

    private List<AssetsStatistics> assetsStatisticsList;

    public AssetsStatisticsHeader getAssetsStatisticsHeader() {
        return assetsStatisticsHeader;
    }

    public void setAssetsStatisticsHeader(AssetsStatisticsHeader assetsStatisticsHeader) {
        this.assetsStatisticsHeader = assetsStatisticsHeader;
    }

    public List<AssetsStatistics> getAssetsStatisticsList() {
        return assetsStatisticsList;
    }

    public void setAssetsStatisticsList(List<AssetsStatistics> assetsStatisticsList) {
        this.assetsStatisticsList = assetsStatisticsList;
    }
}

package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.data.repo.AssetsStatisticsRepo;
import com.fafabtc.data.data.repo.BlockchainAssetsRepo;
import com.fafabtc.data.global.AssetsStateRepository;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.vo.AssetsStatistics;
import com.fafabtc.data.model.vo.AssetsStatisticsHeader;
import com.fafabtc.data.model.vo.AssetsStatisticsHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/10.
 */

public class AssetsViewModel extends ViewModel {

    @Inject
    BlockchainAssetsRepo blockchainAssetsRepo;

    @Inject
    AccountAssetsRepo accountAssetsRepo;

    @Inject
    AssetsStatisticsRepo assetsStatisticsRepo;

    @Inject
    AssetsStateRepository assetsStateRepository;

    MutableLiveData<Double> totalMarketValue = new MutableLiveData<>();
    MutableLiveData<AssetsStatisticsHolder> statisticsHolderData = new MutableLiveData<>();
    MutableLiveData<String> updateTime = new MutableLiveData<>();

    @Inject
    public AssetsViewModel() {
    }

    public void updateStatistics() {
        final BiFunction<List<AssetsStatistics>, Double, AssetsStatisticsHolder> zipFunction =
                new BiFunction<List<AssetsStatistics>, Double, AssetsStatisticsHolder>() {
                    @Override
                    public AssetsStatisticsHolder apply(List<AssetsStatistics> assetsStatistics, Double aDouble) throws Exception {
                        double total = aDouble;
                        List<AssetsStatistics> statisticsRetuls = new ArrayList<>();
                        for (AssetsStatistics statistics : assetsStatistics) {
                            double volume = (statistics.getAvailable() + statistics.getLocked()) * statistics.getUsdtLast();
                            if (volume > 0) {
                                statisticsRetuls.add(statistics);
                                total += volume;
                            }
                        }
                        totalMarketValue.postValue(total);
                        AssetsStatisticsHolder holder = new AssetsStatisticsHolder();
                        AssetsStatisticsHeader header = new AssetsStatisticsHeader();
                        header.setBalanceTotal(aDouble);
                        holder.setAssetsStatisticsHeader(header);
                        holder.setAssetsStatisticsList(statisticsRetuls);
                        return holder;
                    }
                };
        accountAssetsRepo.getCurrent()
                .flatMap(new Function<AccountAssets, SingleSource<AssetsStatisticsHolder>>() {
                    @Override
                    public SingleSource<AssetsStatisticsHolder> apply(AccountAssets accountAssets) throws Exception {
                        return assetsStatisticsRepo
                                .getAccountStatistics(accountAssets.getUuid())
                                .zipWith(blockchainAssetsRepo.getUsdtBalanceFromAccount(accountAssets.getUuid()), zipFunction);
                    }
                })
                .subscribeOn(Schedulers.from(ExecutorManager.getSTATISTICS()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AssetsStatisticsHolder>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Timber.d("onSubscribe");
                    }

                    @Override
                    public void onSuccess(AssetsStatisticsHolder assetsStatistics) {
                        statisticsHolderData.setValue(assetsStatistics);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void loadUpdateTime() {
        assetsStateRepository.getFormatedUpdateTime()
//                .compose(RxUtils.<String>singleAsyncIO())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        updateTime.setValue(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public MutableLiveData<Double> getTotalMarketValue() {
        return totalMarketValue;
    }

    public MutableLiveData<AssetsStatisticsHolder> getStatisticsHolderData() {
        return statisticsHolderData;
    }

    public MutableLiveData<String> getUpdateTime() {
        return updateTime;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}

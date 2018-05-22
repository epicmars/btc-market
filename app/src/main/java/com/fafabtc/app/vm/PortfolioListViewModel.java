package com.fafabtc.app.vm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.global.AssetsStateRepository;
import com.fafabtc.data.data.repo.PortfolioRepo;
import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.domain.model.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/17.
 */

public class PortfolioListViewModel extends ViewModel {

    @Inject
    PortfolioRepo portfolioRepo;

    private MutableLiveData<Resource<List<Portfolio>>> portfolioList;


    @Inject
    public PortfolioListViewModel() {
        portfolioList = new MutableLiveData<>();
    }


    public void loadPortfolioList() {
        portfolioRepo.getAll()
                .compose(RxUtils.<List<Portfolio>>singleAsyncIO())
                .subscribe(new SingleObserver<List<Portfolio>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Portfolio> portfolioList) {
                        PortfolioListViewModel.this.portfolioList.setValue(Resource.success(portfolioList));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        List<Portfolio> assetsList = new ArrayList<>();
                        portfolioList.setValue(Resource.error(e.getMessage(), assetsList));
                    }
                });
    }

    public MutableLiveData<Resource<List<Portfolio>>> getPortfolioList() {
        return portfolioList;
    }


}

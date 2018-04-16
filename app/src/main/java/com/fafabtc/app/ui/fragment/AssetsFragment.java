package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentAssetsBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.AssetsStatisticsHeaderViewHolder;
import com.fafabtc.app.ui.viewholder.AssetsStatisticsViewHolder;
import com.fafabtc.app.vm.PortfolioViewModel;
import com.fafabtc.app.vm.AssetsViewModel;
import com.fafabtc.common.utils.NumberUtils;
import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.data.model.vo.AssetsStatisticsHolder;

/**
 * Created by jastrelax on 2018/1/6.
 */

@BindLayout(R.layout.fragment_assets)
public class AssetsFragment extends BaseFragment<FragmentAssetsBinding> {

    private PortfolioViewModel portfolioViewModel;
    private AssetsViewModel viewModel;
    private RecyclerAdapter adapter;

    public static AssetsFragment newInstance() {
        Bundle args = new Bundle();
        AssetsFragment fragment = new AssetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // int statusBarHeight = UiUtils.getStatusBarHeight(getContext());
        // binding.headerContainer.setMinimumHeight(statusBarHeight);
        getContext().registerReceiver(tickerUpdateReceiver, new IntentFilter(){
            {
                addAction(DataBroadcasts.Actions.ACTION_DATA_INITIALIZED);
                addAction(DataBroadcasts.Actions.ACTION_TICKER_UPDATED);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setAutoMeasureEnabled(false);
        binding.recyclerStatistics.setLayoutManager(llm);

        adapter = new RecyclerAdapter();
        adapter.register(AssetsStatisticsHeaderViewHolder.class,
                AssetsStatisticsViewHolder.class);
        binding.recyclerStatistics.setAdapter(adapter);

        portfolioViewModel = getViewModelOfActivity(PortfolioViewModel.class);
        portfolioViewModel.isCurrentPortfolioChanged().observe(getActivity(), currentPortfolioChangeObserver);
        portfolioViewModel.getCurrentPortfolio().observe(this, currentPortfolioObserver);

        viewModel = getViewModel(AssetsViewModel.class);
        viewModel.getTotalMarketValue().observe(this, totalVolumeObserver);
        viewModel.getStatisticsHolderData().observe(this, statisticsObserver);
        viewModel.getUpdateTime().observe(this, updateTimeObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        showCurrentAssets();
    }

    private Observer<Portfolio> currentPortfolioObserver = new Observer<Portfolio>() {
        @Override
        public void onChanged(@Nullable Portfolio portfolio) {
            binding.tvAssetsName.setText(getString(R.string.assets_name_format, portfolio.getName()));
        }
    };

    private Observer<AssetsStatisticsHolder> statisticsObserver = new Observer<AssetsStatisticsHolder>() {
        @Override
        public void onChanged(@Nullable AssetsStatisticsHolder assetsStatistics) {
            adapter.setPayloads(assetsStatistics.getAssetsStatisticsHeader());
            adapter.addPayloads(assetsStatistics.getAssetsStatisticsList());
        }
    };

    private Observer<Double> totalVolumeObserver = new Observer<Double>() {
        @Override
        public void onChanged(@Nullable Double aDouble) {
            binding.tvTotal.setText(NumberUtils.formatBalance(aDouble));
        }
    };

    private Observer<Boolean> currentPortfolioChangeObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            viewModel.updateStatistics();
            portfolioViewModel.loadCurrentAccount();
        }
    };

    private Observer<String> updateTimeObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            if (TextUtils.isEmpty(s)) return;
            binding.tvUpdateTime.setText(getString(R.string.update_time_format, s));
        }
    };

    private BroadcastReceiver tickerUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showCurrentAssets();
        }
    };

    private void showCurrentAssets() {
        viewModel.updateStatistics();
        viewModel.loadUpdateTime();
        portfolioViewModel.loadCurrentAccount();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(tickerUpdateReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // called when a new instance will be created
        // this method may be called at any time before onDestroy()
    }
}

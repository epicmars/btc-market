package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcast;
import com.fafabtc.app.databinding.FragmentAssetsBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.AssetsStatisticsHeaderViewHolder;
import com.fafabtc.app.ui.viewholder.AssetsStatisticsViewHolder;
import com.fafabtc.app.utils.UiUtils;
import com.fafabtc.app.vm.AccountViewModel;
import com.fafabtc.app.vm.AssetsViewModel;
import com.fafabtc.common.utils.NumberUtils;
import com.fafabtc.data.model.vo.AssetsStatisticsHolder;

/**
 * Created by jastrelax on 2018/1/6.
 */

@BindLayout(R.layout.fragment_assets)
public class AssetsFragment extends BaseFragment<FragmentAssetsBinding> {

    private AccountViewModel accountViewModel;
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
        int statusBarHeight = UiUtils.getStatusBarHeight(getContext());
        binding.headerContainer.setMinimumHeight(statusBarHeight);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setAutoMeasureEnabled(false);
        binding.recyclerStatistics.setLayoutManager(llm);
        binding.recyclerStatistics.addItemDecoration(new DividerItemDecoration(getContext(), llm.getOrientation()));

        adapter = new RecyclerAdapter();
        adapter.register(AssetsStatisticsHeaderViewHolder.class,
                AssetsStatisticsViewHolder.class);
        binding.recyclerStatistics.setAdapter(adapter);

        accountViewModel = getViewModelOfActivity(AccountViewModel.class);
        accountViewModel.getCurrentAccountChanged().observe(getActivity(), currentAccountChangeObserver);

        viewModel = getViewModel(AssetsViewModel.class);
        viewModel.getTotalVolume().observe(this, totalVolumeObserver);
        viewModel.getStatisticsHolderData().observe(this, statisticsObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.updateStatistics();
        getContext().registerReceiver(receiver, new IntentFilter(Broadcast.Actions.ACTION_TICKER_UPDATED));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(receiver);
    }

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
            binding.tvTotal.setText(NumberUtils.formatCurrency(aDouble));
        }
    };

    private Observer<Boolean> currentAccountChangeObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            viewModel.updateStatistics();
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewModel.updateStatistics();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // called when a new instance will be created
        // this method may be called at any time before onDestroy()
    }
}

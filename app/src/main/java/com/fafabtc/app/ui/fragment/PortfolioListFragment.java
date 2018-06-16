package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentPortfolioListBinding;
import com.fafabtc.app.ui.base.BaseDialogFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.PortfolioViewHolder;
import com.fafabtc.app.vm.PortfolioListViewModel;
import com.fafabtc.app.vm.PortfolioViewModel;
import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.base.model.Resource;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/17.
 */
@BindLayout(R.layout.fragment_portfolio_list)
public class PortfolioListFragment extends BaseDialogFragment<FragmentPortfolioListBinding>{

    private RecyclerAdapter adapter;

    private PortfolioListViewModel viewModel;

    private PortfolioViewModel portfolioViewModel;

    public static PortfolioListFragment newInstance() {
        Bundle args = new Bundle();
        PortfolioListFragment fragment = new PortfolioListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme_Dialog);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getContext().registerReceiver(initiateReceiver, new IntentFilter(){
            {
                addAction(DataBroadcasts.Actions.ACTION_DATA_INITIALIZED);
            }
        });

        portfolioViewModel = getViewModelOfActivity(PortfolioViewModel.class);
        viewModel = getViewModelOfActivity(PortfolioListViewModel.class);
        viewModel.getPortfolioList().observe(this, accountListObserver);

        adapter = new RecyclerAdapter();
        adapter.register(PortfolioViewHolder.class);
        binding.recyclerPortfolio.setNestedScrollingEnabled(false);
        binding.recyclerPortfolio.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPortfolio.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerPortfolio.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                // do not update accout list again, it's a loop
                portfolioViewModel.currentAccountChanged();
            }
        });
        viewModel.loadPortfolioList();
    }

    @Override
    public void onStart() {
        super.onStart();
        // return from assets creation activity, should be updated.
        viewModel.loadPortfolioList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(initiateReceiver);
    }

    private BroadcastReceiver initiateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewModel.loadPortfolioList();
        }
    };

    private Observer<Resource<List<Portfolio>>> accountListObserver = new Observer<Resource<List<Portfolio>>>() {
        @Override
        public void onChanged(@Nullable Resource<List<Portfolio>> portfolioResource) {
            if (portfolioResource != null && portfolioResource.isSuccess()) {
                adapter.setPayloads(portfolioResource.data);
            }
        }
    };
}

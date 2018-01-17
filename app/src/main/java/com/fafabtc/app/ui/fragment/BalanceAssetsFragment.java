package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentBalanceAssetsBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.BlockchainAssetsViewHolder;
import com.fafabtc.app.vm.BalanceAssetsViewModel;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/14.
 */
@BindLayout(R.layout.fragment_balance_assets)
public class BalanceAssetsFragment extends BaseFragment<FragmentBalanceAssetsBinding> {

    private static final String ARGS_ACCOUNT_ASSETS = "BalanceAssetsFragment.ARGS_ACCOUNT_ASSETS";
    private static final String ARGS_EXCHANGE = "BalanceAssetsFragment.ARGS_EXCHANGE";

    private RecyclerAdapter adapter;
    private BalanceAssetsViewModel viewModel;

    public static BalanceAssetsFragment newInstance(AccountAssets accountAssets, Exchange exchange) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ACCOUNT_ASSETS, accountAssets);
        args.putParcelable(ARGS_EXCHANGE, exchange);
        BalanceAssetsFragment fragment = new BalanceAssetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AccountAssets accountAssets = getArguments().getParcelable(ARGS_ACCOUNT_ASSETS);
        Exchange exchange = getArguments().getParcelable(ARGS_EXCHANGE);

        adapter = new RecyclerAdapter();
        adapter.register(BlockchainAssetsViewHolder.class);
        initRecyclerView(binding.recyclerBalanceAssets, adapter);

        viewModel = getViewModel(BalanceAssetsViewModel.class);
        viewModel.setAccountAssets(accountAssets);
        viewModel.setExchange(exchange);
        viewModel.getBalanceAssetsData().observe(this, balanceAssetsObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.loadBalanceAssets();
    }

    private Observer<List<BlockchainAssets>> balanceAssetsObserver = new Observer<List<BlockchainAssets>>() {
        @Override
        public void onChanged(@Nullable List<BlockchainAssets> blockchainAssets) {
            adapter.setPayloads(blockchainAssets);
        }
    };

    private void initRecyclerView(RecyclerView view, RecyclerAdapter adapter) {
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        view.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        view.setAdapter(adapter);
    }
}

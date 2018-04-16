package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentBlockchainAssetsBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.BlockchainAssetsViewHolder;
import com.fafabtc.app.vm.BlockchainAssetsViewModel;
import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Exchange;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/14.
 */
@BindLayout(R.layout.fragment_blockchain_assets)
public class BlockchainAssetsFragment extends BaseFragment<FragmentBlockchainAssetsBinding> {

    private static final String ARGS_PORTFOLIO = "BalanceAssetsFragment.ARGS_PORTFOLIO";
    private static final String ARGS_EXCHANGE = "BalanceAssetsFragment.ARGS_EXCHANGE";

    private RecyclerAdapter adapter;

    private BlockchainAssetsViewModel viewModel;

    public static BlockchainAssetsFragment newInstance(Portfolio portfolio, Exchange exchange) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_PORTFOLIO, portfolio);
        args.putParcelable(ARGS_EXCHANGE, exchange);
        BlockchainAssetsFragment fragment = new BlockchainAssetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Portfolio portfolio = getArguments().getParcelable(ARGS_PORTFOLIO);
        Exchange exchange = getArguments().getParcelable(ARGS_EXCHANGE);

        adapter = new RecyclerAdapter();
        adapter.register(BlockchainAssetsViewHolder.class);
        initRecyclerView(binding.recyclerBlockchainAssets, adapter);

        viewModel = getViewModel(BlockchainAssetsViewModel.class);
        viewModel.setPortfolio(portfolio);
        viewModel.setExchange(exchange);
        viewModel.getBlockchainAssetsData().observe(this, blockchainAssetsObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.loadBlockchainAssets();
    }

    private Observer<List<BlockchainAssets>> blockchainAssetsObserver = new Observer<List<BlockchainAssets>>() {
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

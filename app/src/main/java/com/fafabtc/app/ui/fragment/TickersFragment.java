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
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcast;
import com.fafabtc.app.databinding.FragmentTickersBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.TickerViewHolder;
import com.fafabtc.app.vm.TickersViewModel;
import com.fafabtc.data.model.entity.exchange.Ticker;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/6.
 */
@BindLayout(R.layout.fragment_tickers)
public class TickersFragment extends BaseFragment<FragmentTickersBinding> {

    private static final String ARGS_EXCHANGE = "TickersFragment.ARGS_EXCHANGE";

    private RecyclerAdapter mAdapter;
    private TickersViewModel viewModel;

    private String exchange;

    public static TickersFragment newInstance(String exchange) {
        Bundle args = new Bundle();
        args.putString(ARGS_EXCHANGE, exchange);
        TickersFragment fragment = new TickersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exchange = getArguments().getString(ARGS_EXCHANGE);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_ticker);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter();
        mAdapter.register(TickerViewHolder.class);
        recyclerView.setAdapter(mAdapter);

        viewModel = getViewModel(TickersViewModel.class);
        viewModel.setExchange(exchange);
        viewModel.getTickers().observe(this, tickerListObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.updateTickers();
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

    private Observer<List<Ticker>> tickerListObserver = new Observer<List<Ticker>>() {
        @Override
        public void onChanged(@Nullable List<Ticker> tickerList) {
            mAdapter.setPayloads(tickerList);
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewModel.updateTickers();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

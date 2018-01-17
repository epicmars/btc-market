package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.app.databinding.FragmentTickersBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BaseListAdapter;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.viewholder.TickerViewHolder;
import com.fafabtc.app.vm.TickersViewModel;
import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.model.entity.exchange.Ticker;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/6.
 */
@BindLayout(R.layout.fragment_tickers)
public class TickersFragment extends BaseFragment<FragmentTickersBinding> {

    private static final String ARGS_EXCHANGE = "TickersFragment.ARGS_EXCHANGE";

    private BaseListAdapter mAdapter;
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

        mAdapter = new BaseListAdapter();
        mAdapter.register(TickerViewHolder.class);
        binding.listTicker.setAdapter(mAdapter);

        viewModel = getViewModel(TickersViewModel.class);
        viewModel.setExchange(exchange);
        viewModel.getTickers().observe(this, tickerListObserver);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.updateTickers();
        getContext().registerReceiver(receiver, new IntentFilter(){
            {
                addAction(DataBroadcasts.Actions.ACTION_DATA_INITIALIZED);
                addAction(Broadcasts.Actions.ACTION_TICKER_UPDATED);
            }
        });
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

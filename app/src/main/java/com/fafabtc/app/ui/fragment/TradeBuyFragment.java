package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcast;
import com.fafabtc.app.databinding.FragmentTradeBuyBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.vm.TradeBuyViewModel;
import com.fafabtc.app.vm.TradeViewModel;
import com.fafabtc.common.utils.NumberUtils;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Ticker;
import com.fafabtc.domain.model.Resource;

/**
 * Created by jastrelax on 2018/1/9.
 */
@BindLayout(R.layout.fragment_trade_buy)
public class TradeBuyFragment extends BaseFragment<FragmentTradeBuyBinding>{

    private static final String ARGS_TICKER = "TradeBuyFragment.ARGS_TICKER";

    private TradeBuyViewModel viewModel;
    private TradeViewModel tradeViewModel;
    private Ticker ticker;

    public static TradeBuyFragment newInstance(Ticker ticker) {
        
        Bundle args = new Bundle();
        args.putParcelable(ARGS_TICKER, ticker);
        TradeBuyFragment fragment = new TradeBuyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ticker = getArguments().getParcelable(ARGS_TICKER);

        // init view
        binding.tradeView.etPrice.setHint(R.string.price_ask);
        binding.tradeView.etQuantity.setHint(R.string.quantity);
        binding.tradeView.etVolume.setHint(R.string.volume);
        binding.tradeView.btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double price = NumberUtils.parseDouble(binding.tradeView.etPrice.getText().toString());
                Double quantity = NumberUtils.parseDouble(binding.tradeView.etQuantity.getText().toString());
                if (!NumberUtils.isPositive(price) || !NumberUtils.isPositive(quantity)) {

                } else {
                    viewModel.buyBlockchainAssets(price, quantity);
                }
            }
        });
        binding.tradeView.tvLabelBalanceAvailable.setText(getString(R.string.available_format, ticker.getQuote().toUpperCase()));

        // observer
        tradeViewModel = getViewModelOfActivity(TradeViewModel.class);
        tradeViewModel.getTickerLiveData().observe(getActivity(), tickerObserver);

        viewModel = getViewModel(TradeBuyViewModel.class);
        viewModel.setTicker(ticker);
        viewModel.updateQuoteBalance();
        viewModel.getBalanceAssets().observe(this, balanceObserver);
        viewModel.getIsOrderCreated().observe(this, orderCreationObserver);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private boolean tickerLoaded = false;
    private Observer<Ticker> tickerObserver = new Observer<Ticker>() {
        @Override
        public void onChanged(@Nullable Ticker ticker) {
            if (TextUtils.isEmpty(binding.tradeView.etPrice.getText()) && !tickerLoaded) {
                tickerLoaded = false;
                String priceAsk = NumberUtils.formatCurrency(ticker.getAsk());
                binding.tradeView.etPrice.setText(priceAsk);
                binding.tradeView.etPrice.setSelection(priceAsk.length());
            }
        }
    };

    private Observer<Resource<Boolean>> orderCreationObserver = new Observer<Resource<Boolean>>() {
        @Override
        public void onChanged(@Nullable Resource<Boolean> booleanResource) {
            if (booleanResource.isSuccess()) {
                getContext().sendBroadcast(new Intent(Broadcast.Actions.ACTION_ORDER_CREATED));
            }
        }
    };

    private Observer<BlockchainAssets> balanceObserver = new Observer<BlockchainAssets>() {
        @Override
        public void onChanged(@Nullable BlockchainAssets balanceAssets) {
            binding.tradeView.tvBalanceAvailable.setText(NumberUtils.formatBlockchainQuantity(balanceAssets.getAvailable()));
        }
    };
}

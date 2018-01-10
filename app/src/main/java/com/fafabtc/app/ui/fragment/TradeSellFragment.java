package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentTradeSellBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.vm.TradeSellViewModel;
import com.fafabtc.app.vm.TradeViewModel;
import com.fafabtc.common.utils.NumberUtils;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Ticker;

/**
 * Created by jastrelax on 2018/1/9.
 */
@BindLayout(R.layout.fragment_trade_sell)
public class TradeSellFragment extends BaseFragment<FragmentTradeSellBinding> {

    private static final String ARGS_TICKER = "TradeSellFragment.ARGS_TICKER";

    private TradeViewModel tradeViewModel;
    private Ticker ticker;
    private TradeSellViewModel viewModel;

    public static TradeSellFragment newInstance(Ticker ticker) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_TICKER, ticker);
        TradeSellFragment fragment = new TradeSellFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ticker = getArguments().getParcelable(ARGS_TICKER);

        // init view
        binding.tradeView.etPrice.setHint(R.string.price_bid);
        binding.tradeView.etQuantity.setHint(R.string.quantity);
        binding.tradeView.etVolume.setHint(R.string.volume);
        binding.tradeView.btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double price = NumberUtils.parseDouble(binding.tradeView.etPrice.getText().toString());
                Double quantity = NumberUtils.parseDouble(binding.tradeView.etQuantity.getText().toString());
                if (!NumberUtils.isPositive(price) || !NumberUtils.isPositive(quantity)) {

                } else {
                    viewModel.sellBlockchainAssets(price, quantity);
                }
            }
        });
        binding.tradeView.tvLabelBalanceAvailable.setText(getString(R.string.available_format, ticker.getBase().toUpperCase()));

        // observer
        tradeViewModel = getViewModelOfActivity(TradeViewModel.class);
        tradeViewModel.getTickerLiveData().observe(getActivity(), tickerObserver);

        viewModel = getViewModel(TradeSellViewModel.class);
        viewModel.setTicker(ticker);
        viewModel.updateBaseBalance();
        viewModel.getBaseBlockchainAssets()
                .observe(this, balanceObserver);
    }

    private boolean tickerLoaded = false;
    private Observer<Ticker> tickerObserver = new Observer<Ticker>() {
        @Override
        public void onChanged(@Nullable Ticker ticker) {
            if (TextUtils.isEmpty(binding.tradeView.etPrice.getText()) && !tickerLoaded) {
                tickerLoaded = true;
                String priceBid = NumberUtils.formatCurrency(ticker.getBid());
                binding.tradeView.etPrice.setText(priceBid);
                binding.tradeView.etPrice.setSelection(priceBid.length());
            }
        }
    };

    private Observer<BlockchainAssets> balanceObserver = new Observer<BlockchainAssets>() {
        @Override
        public void onChanged(@Nullable BlockchainAssets balanceAssets) {
            if (balanceAssets == null) return;
            binding.tradeView.tvBalanceAvailable.setText(NumberUtils.formatBlockchainQuantity(balanceAssets.getAvailable()));
        }
    };
}

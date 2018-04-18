package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.app.databinding.FragmentTradeSellBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.vm.TradeViewModel;
import com.fafabtc.common.utils.NumberUtils;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.data.model.entity.exchange.Ticker;
import com.fafabtc.domain.model.Resource;

/**
 * Created by jastrelax on 2018/1/9.
 */
@BindLayout(R.layout.fragment_trade_sell)
public class TradeSellFragment extends BaseFragment<FragmentTradeSellBinding> {

    private static final String ARGS_TICKER = "TradeSellFragment.ARGS_TICKER";

    private TradeViewModel viewModel;
    private Ticker ticker;

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
        String priceBid = NumberUtils.formatPrice(ticker.getBid());
        binding.tradeView.etPrice.setText(priceBid);
        binding.tradeView.etPrice.setSelection(priceBid.length());
        binding.tradeView.etPrice.setHint(R.string.price_ask);
        binding.tradeView.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double price = NumberUtils.parseDouble(s.toString());
                Double quantity = getQuantityInput();
                if (null != price && null != quantity) {
                    setVolumeInput(price * quantity);
                }
            }
        });

        binding.tradeView.etQuantity.setHint(R.string.quantity);
        binding.tradeView.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double quantity = NumberUtils.parseDouble(s.toString());
                Double price = getPriceInput();
                if (null != price && null != quantity) {
                    setVolumeInput(price * quantity);
                }
            }
        });

        binding.tradeView.etVolume.setHint(R.string.volume);
        binding.tradeView.tvLabelPrice.setText(getString(R.string.trade_label_price_ask, ticker.getQuote()));
        binding.tradeView.tvLabelQuantity.setText(getString(R.string.trade_label_commission_quantity, ticker.getBase()));
        binding.tradeView.tvLabelCommissionVolume.setText(getString(R.string.trade_label_commission_volume, ticker.getQuote()));
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
        viewModel = getViewModelOfActivity(TradeViewModel.class);
        viewModel.setTicker(ticker);
        viewModel.updateBaseBalance();

        viewModel.getTickerLiveData().observe(this, tickerObserver);
        viewModel.getBaseBlockchainAssets().observe(this, balanceObserver);
        viewModel.getIsOrderCreated().observe(this, orderCreationObserver);
    }

    private Double getPriceInput() {
        return NumberUtils.parseDouble(binding.tradeView.etPrice.getText().toString());
    }

    private Double getQuantityInput() {
        return NumberUtils.parseDouble(binding.tradeView.etQuantity.getText().toString());
    }

    private Double getVolumeInput() {
        return NumberUtils.parseDouble(binding.tradeView.etVolume.getText().toString());
    }

    private void setQuantityInput(Double quantity) {
        binding.tradeView.etQuantity.setText(NumberUtils.formatBlockchainQuantity(quantity));
    }

    private void setVolumeInput(Double volume) {
        binding.tradeView.etVolume.setText(NumberUtils.formatPrice(volume));
    }

    private Observer<Resource<Ticker>> tickerObserver = new Observer<Resource<Ticker>>() {
        @Override
        public void onChanged(@Nullable Resource<Ticker> ticker) {
        }
    };

    private Observer<Resource<BlockchainAssets>> balanceObserver = new Observer<Resource<BlockchainAssets>>() {
        @Override
        public void onChanged(@Nullable Resource<BlockchainAssets> balanceAssets) {
            if (balanceAssets == null) return;
            binding.tradeView.tvBalanceAvailable.setText(NumberUtils.formatBlockchainQuantity(balanceAssets.data.getAvailable()));
        }
    };

    private Observer<Resource<Boolean>> orderCreationObserver = new Observer<Resource<Boolean>>() {
        @Override
        public void onChanged(@Nullable Resource<Boolean> booleanResource) {
            if (booleanResource != null && booleanResource.isSuccess()) {
                getContext().sendBroadcast(new Intent(Broadcasts.Actions.ACTION_ORDER_CREATED));
                viewModel.updateBaseBalance();
            }
        }
    };
}

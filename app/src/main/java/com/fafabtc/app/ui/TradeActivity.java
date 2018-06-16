package com.fafabtc.app.ui;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ActivityTradeBinding;
import com.fafabtc.app.ui.base.BaseActivity;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.fragment.TradeFragment;
import com.fafabtc.app.vm.TradeViewModel;
import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.model.entity.exchange.Ticker;
import com.fafabtc.base.model.Resource;

@BindLayout(R.layout.activity_trade)
public class TradeActivity extends BaseActivity<ActivityTradeBinding> {

    public static final String EXTAR_TICKER = "TradeActivity.EXTAR_TICKER";

    private Ticker ticker;
    private TradeViewModel viewModel;

    public static void start(Context context, Ticker ticker) {
        Intent starter = new Intent(context, TradeActivity.class);
        starter.putExtra(EXTAR_TICKER, ticker);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            ticker = intent.getParcelableExtra(EXTAR_TICKER);
        }
        setTitle(getString(R.string.trade_exchange_pair_format, ticker.getBase(), ticker.getQuote()).toUpperCase());

        viewModel = getViewModel(TradeViewModel.class);
        viewModel.setTicker(ticker);
        viewModel.getTickerLiveData().observe(this, tickerObserver);
        viewModel.refreshTicker();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.trade_content, TradeFragment.newInstance(ticker));
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(tickerReceiver, new IntentFilter(DataBroadcasts.Actions.ACTION_TICKER_UPDATED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(tickerReceiver);
    }

    private BroadcastReceiver tickerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewModel.refreshTicker();
        }
    };

    private Observer<Resource<Ticker>> tickerObserver = new Observer<Resource<Ticker>>() {
        @Override
        public void onChanged(@Nullable Resource<Ticker> ticker) {
            if (ticker != null && ticker.isSuccess())
                binding.setTicker(ticker.data);
        }
    };
}

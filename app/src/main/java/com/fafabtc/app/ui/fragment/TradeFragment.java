package com.fafabtc.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentTradeBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.data.model.entity.exchange.Ticker;

@BindLayout(R.layout.fragment_trade)
public class TradeFragment extends BaseFragment<FragmentTradeBinding> {

    private static final String ARGS_TICKER = "TradeFragment.ARGS_TICKER";

    private Ticker ticker;

    public static TradeFragment newInstance(Ticker ticker) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_TICKER, ticker);
        TradeFragment fragment = new TradeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ticker = getArguments().getParcelable(ARGS_TICKER);
        binding.pagerTrade.setAdapter(new TradeFragmentPagerAdapter(getChildFragmentManager()));
    }

    private class TradeFragmentPagerAdapter extends FragmentPagerAdapter {

        private final int[] titlesRes = {R.string.buy, R.string.sell, R.string.orders};

        public TradeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TradeBuyFragment.newInstance(ticker);
                case 1:
                    return TradeSellFragment.newInstance(ticker);
                case 2:
                    return OrdersFragment.newInstance(null, ticker.getExchange(), ticker.getPair());
                default:
                    return ErrorFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return titlesRes.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(titlesRes[position]);
        }
    }
}

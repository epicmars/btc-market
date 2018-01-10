package com.fafabtc.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentTickerPagerBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.utils.UiUtils;
import com.fafabtc.binance.data.repo.BinanceRepo;
import com.fafabtc.gateio.data.repo.GateioRepo;

/**
 * Created by jastrelax on 2018/1/10.
 */
@BindLayout(R.layout.fragment_ticker_pager)
public class TickerPagerFragment extends BaseFragment<FragmentTickerPagerBinding>{

    private static String[] EXCHANGES = {GateioRepo.GATEIO_EXCHANGE,
            BinanceRepo.BINANCE_EXCHANGE};

    private TickerPagerAdapter adapter;

    public static TickerPagerFragment newInstance() {
        Bundle args = new Bundle();
        TickerPagerFragment fragment = new TickerPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tabs.setupWithViewPager(binding.pagerTickers);
        binding.llTitle.setPadding(0, UiUtils.getStatusBarHeight(getContext()), 0, 0);

        adapter = new TickerPagerAdapter(getChildFragmentManager());
        binding.pagerTickers.setAdapter(adapter);
    }


    private class TickerPagerAdapter extends FragmentPagerAdapter {

        public TickerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TickersFragment.newInstance(EXCHANGES[position]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return EXCHANGES[position];
        }

        @Override
        public int getCount() {
            return EXCHANGES.length;
        }
    }
}

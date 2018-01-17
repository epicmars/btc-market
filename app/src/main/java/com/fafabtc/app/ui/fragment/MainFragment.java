package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentMainBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.utils.UiUtils;
import com.fafabtc.app.vm.MainViewModel;
import com.fafabtc.data.consts.DataBroadcasts;

/**
 * Created by jastrelax on 2018/1/7.
 */
@BindLayout(R.layout.fragment_main)
public class MainFragment extends BaseFragment<FragmentMainBinding> {

    private static final int[] BOTTOM_NAV_IDS = {R.id.main_bottom_nav_assets,
            R.id.main_bottom_nav_tickers,
            R.id.main_bottom_nav_account};
    private static final SparseIntArray BOTTOM_NAV_POSITION_MAP = new SparseIntArray(){
        {
            for (int pos = 0; pos < BOTTOM_NAV_IDS.length; pos++) {
                put(BOTTOM_NAV_IDS[pos], pos);
            }
        }
    };

    private MainViewModel mainViewModel;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getContext().registerReceiver(initiateReceiver, new IntentFilter(){
            {
                addAction(DataBroadcasts.Actions.ACTION_INITIATE_EXCHANGE);
                addAction(DataBroadcasts.Actions.ACTION_INITIATE_ASSETS);
                addAction(DataBroadcasts.Actions.ACTION_DATA_INITIALIZED);
            }
        });

        mainViewModel = getViewModel(MainViewModel.class);
        mainViewModel.isDataInitialized().observe(this, initiateObserver);
        mainViewModel.queryIsDataInitialized();

        binding.pagerMain.setOffscreenPageLimit(2);
        binding.pagerMain.setAdapter(new MainFragmentPagerAdapter(getChildFragmentManager()));
        binding.pagerMain.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position >= 0 && position < BOTTOM_NAV_IDS.length)
                    binding.bottomNavMain.setSelectedItemId(BOTTOM_NAV_IDS[position]);
            }
        });

        binding.bottomNavMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                binding.pagerMain.setCurrentItem(BOTTOM_NAV_POSITION_MAP.get(id));
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(initiateReceiver);
    }

    private Observer<Boolean> initiateObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            if (aBoolean != null && aBoolean) {
                binding.tvStateTip.setVisibility(View.GONE);
            } else {
                binding.tvStateTip.setPadding(0, UiUtils.getStatusBarHeight(getContext()), 0, getResources().getDimensionPixelSize(R.dimen.tv_padding_small));
                binding.tvStateTip.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver initiateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null) {
                switch (intent.getAction()) {
                    case DataBroadcasts.Actions.ACTION_INITIATE_EXCHANGE:
                        binding.tvStateTip.setText("初始化交易所数据...");
                        break;
                    case DataBroadcasts.Actions.ACTION_INITIATE_ASSETS:
                        binding.tvStateTip.setText("初始化资产...");
                        break;
                    case DataBroadcasts.Actions.ACTION_FETCH_TICKERS:
                        binding.tvStateTip.setText("获取最新行情...");
                        break;
                    case DataBroadcasts.Actions.ACTION_DATA_INITIALIZED:
                        binding.tvStateTip.setVisibility(View.GONE);
                        break;
                }
            }
        }
    };

    private class MainFragmentPagerAdapter extends FragmentPagerAdapter {

        private int[] titles = {R.string.assets, R.string.tickers, R.string.mine};

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AssetsFragment.newInstance();
                case 1:
                    return TickerPagerFragment.newInstance();
                case 2:
                    return AccountFragment.newInstance();
                default:
                    return ErrorFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(titles[position]);
        }
    }
}

package com.fafabtc.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fafabtc.app.R;
import com.fafabtc.binance.data.repo.BinanceRepo;

/**
 * Created by jastrelax on 2018/1/7.
 */
public class MainFragment extends Fragment {

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

    private ViewPager vp;
    private BottomNavigationView bnv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vp = view.findViewById(R.id.pager_main);
        bnv = view.findViewById(R.id.bottom_nav_main);

        vp.setOffscreenPageLimit(2);
        vp.setAdapter(new MainFragmentPagerAdapter(getChildFragmentManager()));
        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position >= 0 && position < BOTTOM_NAV_IDS.length)
                    bnv.setSelectedItemId(BOTTOM_NAV_IDS[position]);
            }
        });

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                vp.setCurrentItem(BOTTOM_NAV_POSITION_MAP.get(id));
                return true;
            }
        });
    }

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

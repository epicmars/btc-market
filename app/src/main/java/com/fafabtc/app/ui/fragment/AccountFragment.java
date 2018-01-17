package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.app.databinding.FragmentAccountBinding;
import com.fafabtc.app.di.Injectable;
import com.fafabtc.app.ui.activity.AboutActivity;
import com.fafabtc.app.ui.activity.AccountAssetsCreateActivity;
import com.fafabtc.app.ui.activity.SettingsActivity;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.vm.AccountViewModel;
import com.fafabtc.data.consts.DataBroadcasts;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Injectable
@BindLayout(R.layout.fragment_account)
public class AccountFragment extends BaseFragment<FragmentAccountBinding> {

    private AccountViewModel viewModel;

    private AccountFragmentHandler fragmentHandler;

    public static AccountFragment newInstance() {

        Bundle args = new Bundle();

        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(dataInitializationReceiver, new IntentFilter(DataBroadcasts.Actions.ACTION_DATA_INITIALIZED));
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(dataInitializationReceiver);
    }

    private void init() {
        viewModel = getViewModelOfActivity(AccountViewModel.class);
        viewModel.isCurrentAccountChanged().observe(getActivity(), accountChangeObserver);
        viewModel.isAssetsInitialized().observe(this, assetsInitializedObserver);

        fragmentHandler = new AccountFragmentHandler();
        binding.setHandler(fragmentHandler);
    }

    private BroadcastReceiver dataInitializationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    private Observer<Boolean> assetsInitializedObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            if (aBoolean != null && aBoolean) {
                binding.accountAssetsList.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "资产初始化中", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Observer<Boolean> accountChangeObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean aBoolean) {
            if (aBoolean != null && aBoolean) {
                getContext().sendBroadcast(new Intent(Broadcasts.Actions.ACTION_CURRENT_ASSETS_CHANGED));
            }
        }
    };

    public class AccountFragmentHandler {

        public void onClickAssetsGroup(View view) {
            if (binding.accountAssetsList.getVisibility() == View.VISIBLE) {
                binding.accountAssetsList.setVisibility(View.GONE);
            } else {
                viewModel.loadAccountList();
            }
        }

        public void onClickCreateAssets(View view) {
            AccountAssetsCreateActivity.start(getContext());
        }

        public void onClickAbout(View view) {
            AboutActivity.start(getContext());
        }

        public void onClickSettings(View view) {
            SettingsActivity.start(getContext());
        }

    }
}

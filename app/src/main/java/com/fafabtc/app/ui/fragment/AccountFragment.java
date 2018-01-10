package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcast;
import com.fafabtc.app.databinding.FragmentAccountBinding;
import com.fafabtc.app.di.Injectable;
import com.fafabtc.app.ui.activity.AccountAssetsCreateActivity;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.AccountAssetsViewHolder;
import com.fafabtc.app.vm.AccountViewModel;
import com.fafabtc.common.analysis.AnalysisHelper;
import com.fafabtc.data.model.entity.exchange.AccountAssets;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/8.
 */

@Injectable
@BindLayout(R.layout.fragment_account)
public class AccountFragment extends BaseFragment<FragmentAccountBinding> {

    private AccountViewModel viewModel;

    private RecyclerAdapter adapter;

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
        // return from assets creation activity, should be updated.
        viewModel.updateAccountList();
        getContext().registerReceiver(dataInitializationReceiver, new IntentFilter(Broadcast.Actions.ACTION_DATA_INITIALIZED));
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(dataInitializationReceiver);
    }

    private void init() {
        viewModel = getViewModelOfActivity(AccountViewModel.class);
        viewModel.getAccountAssetsList().observe(getActivity(), accountListObserver);

        fragmentHandler = new AccountFragmentHandler();
        binding.setHandler(fragmentHandler);

        adapter = new RecyclerAdapter();
        adapter.register(AccountAssetsViewHolder.class);
        binding.recyclerAccountAssets.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerAccountAssets.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerAccountAssets.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                // do not update accout list again, it's a loop
                viewModel.currentAccountChanged();
            }
        });
    }

    private BroadcastReceiver dataInitializationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewModel.updateAccountList();
        }
    };

    private Observer<List<AccountAssets>> accountListObserver = new Observer<List<AccountAssets>>() {
        @Override
        public void onChanged(@Nullable List<AccountAssets> accountAssets) {
            adapter.setPayloads(accountAssets);
        }
    };

    public class AccountFragmentHandler {

        public void onClickCreateAssets(View view) {
            AccountAssetsCreateActivity.start(getContext());
        }

        public void onClickUserFeedBack(View view) {
            AnalysisHelper.startFeedBackActivity(getActivity());
        }
    }
}

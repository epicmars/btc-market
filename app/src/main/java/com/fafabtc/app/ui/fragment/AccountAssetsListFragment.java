package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentAccountAssetsListBinding;
import com.fafabtc.app.ui.base.BaseDialogFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.AccountAssetsViewHolder;
import com.fafabtc.app.vm.AccountAssetsViewModel;
import com.fafabtc.app.vm.AccountViewModel;
import com.fafabtc.data.consts.DataBroadcasts;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.domain.model.Resource;

import java.util.List;

/**
 * Created by jastrelax on 2018/1/17.
 */
@BindLayout(R.layout.fragment_account_assets_list)
public class AccountAssetsListFragment extends BaseDialogFragment<FragmentAccountAssetsListBinding>{

    private RecyclerAdapter adapter;

    private AccountAssetsViewModel viewModel;

    private AccountViewModel accountViewModel;

    public static AccountAssetsListFragment newInstance() {
        Bundle args = new Bundle();
        AccountAssetsListFragment fragment = new AccountAssetsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme_Dialog);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getContext().registerReceiver(initiateReceiver, new IntentFilter(){
            {
                addAction(DataBroadcasts.Actions.ACTION_DATA_INITIALIZED);
            }
        });

        accountViewModel = getViewModelOfActivity(AccountViewModel.class);
        viewModel = getViewModelOfActivity(AccountAssetsViewModel.class);
        viewModel.getAccountAssetsList().observe(this, accountListObserver);

        adapter = new RecyclerAdapter();
        adapter.register(AccountAssetsViewHolder.class);
        binding.recyclerAccountAssets.setNestedScrollingEnabled(false);
        binding.recyclerAccountAssets.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerAccountAssets.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerAccountAssets.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                // do not update accout list again, it's a loop
                accountViewModel.currentAccountChanged();
            }
        });
        viewModel.loadAccountAssetsList();
    }

    @Override
    public void onStart() {
        super.onStart();
        // return from assets creation activity, should be updated.
        viewModel.loadAccountAssetsList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(initiateReceiver);
    }

    private BroadcastReceiver initiateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewModel.loadAccountAssetsList();
        }
    };

    private Observer<Resource<List<AccountAssets>>> accountListObserver = new Observer<Resource<List<AccountAssets>>>() {
        @Override
        public void onChanged(@Nullable Resource<List<AccountAssets>> accountAssets) {
            if (accountAssets != null && accountAssets.isSuccess()) {
                adapter.setPayloads(accountAssets.data);
            }
        }
    };
}

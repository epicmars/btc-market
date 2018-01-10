package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentExchangeEntryBinding;
import com.fafabtc.app.di.Injectable;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.ExchangeEntryViewHolder;
import com.fafabtc.app.vm.ExchangeEntryViewModel;
import com.fafabtc.app.vm.ViewModelFactory;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.data.model.vo.ExchangeEntry;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Injectable
@BindLayout(R.layout.fragment_exchange_entry)
public class ExchangeEntryFragment extends BaseFragment<FragmentExchangeEntryBinding> {

    private static final String ARGS_ACCOUNT_ASSETS = "ExchangeEntryFragment.ARGS_ACCOUNT_ASSETS";

    private RecyclerAdapter adapter;

    ExchangeEntryViewModel viewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    public static ExchangeEntryFragment newInstance(AccountAssets accountAssets) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_ACCOUNT_ASSETS, accountAssets);
        ExchangeEntryFragment fragment = new ExchangeEntryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AccountAssets accountAssets = getArguments().getParcelable(ARGS_ACCOUNT_ASSETS);

        adapter = new RecyclerAdapter();
        adapter.register(ExchangeEntryViewHolder.class);
        binding.recyclerExchangeEntry.setAdapter(adapter);
        binding.recyclerExchangeEntry.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerExchangeEntry.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        viewModel = getViewModel(ExchangeEntryViewModel.class);
        viewModel.setAccountAssets(accountAssets);
        viewModel.getExchanges().observe(this, exchangeEntriesObserver);
        viewModel.loadExchanges();
    }

    private Observer<List<ExchangeEntry>> exchangeEntriesObserver = new Observer<List<ExchangeEntry>>() {
        @Override
        public void onChanged(@Nullable List<ExchangeEntry> exchanges) {
            adapter.setPayloads(exchanges);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

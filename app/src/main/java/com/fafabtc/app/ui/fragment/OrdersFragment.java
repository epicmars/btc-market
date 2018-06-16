package com.fafabtc.app.ui.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.FragmentOrdersBinding;
import com.fafabtc.app.ui.base.BaseFragment;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.ui.viewholder.OrderViewHolder;
import com.fafabtc.app.vm.OrdersViewModel;
import com.fafabtc.app.vm.TradeViewModel;
import com.fafabtc.data.model.entity.exchange.Order;
import com.fafabtc.base.model.Resource;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jastrelax on 2018/1/14.
 */
@BindLayout(R.layout.fragment_orders)
public class OrdersFragment extends BaseFragment<FragmentOrdersBinding> {

    private static final String ARGS_ASSETS_UUID = "OrdersFragment.ARGS_ASSETS_UUID";
    private static final String ARGS_EXCHANGE = "OrdersFragment.ARGS_EXCHANGE";
    private static final String ARGS_PAIR = "OrdersFragment.ARGS_PAIR";

    private RecyclerAdapter adapter;

    @Inject
    OrdersViewModel viewModel;

    @Inject
    TradeViewModel tradeViewModel;

    public static OrdersFragment newInstance(String assetsUUID, String exchange, String pair) {
        Bundle args = new Bundle();
        args.putString(ARGS_ASSETS_UUID, assetsUUID);
        args.putString(ARGS_EXCHANGE, exchange);
        args.putString(ARGS_PAIR, pair);
        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String assetsUUID = args.getString(ARGS_ASSETS_UUID);
        String exchange = args.getString(ARGS_EXCHANGE);
        String pair = args.getString(ARGS_PAIR);

        adapter = new RecyclerAdapter();
        adapter.register(OrderViewHolder.class);
        adapter.registerAdapterDataObserver(adapterDataObserver);
        binding.recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerOrders.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerOrders.setAdapter(adapter);

        tradeViewModel = getViewModelOfActivity(TradeViewModel.class);
        tradeViewModel.getIsOrderCreated().observe(this, orderCreationObserver);

        viewModel = getViewModel(OrdersViewModel.class);
        viewModel.setAssetsUUID(assetsUUID);
        viewModel.setExchange(exchange);
        viewModel.setPair(pair);
        viewModel.getOrdersData().observe(this, ordersObserver);
        viewModel.loadOrders();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.unregisterAdapterDataObserver(adapterDataObserver);
    }

    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            tradeViewModel.updateBaseBalance();
            tradeViewModel.updateQuoteBalance();
        }
    };

    private Observer<List<Order>> ordersObserver = new Observer<List<Order>>() {
        @Override
        public void onChanged(@Nullable List<Order> orders) {
            adapter.setPayloads(orders);
        }
    };

    private Observer<Resource<Boolean>> orderCreationObserver = new Observer<Resource<Boolean>>() {
        @Override
        public void onChanged(@Nullable Resource<Boolean> booleanResource) {
            if (booleanResource != null && booleanResource.isSuccess()) {
                viewModel.loadOrders();
            }
        }
    };
}

package com.fafabtc.app.ui.viewholder;

import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ViewHolderOrderBinding;
import com.fafabtc.app.ui.base.BaseViewHolder;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.data.model.entity.exchange.Order;

/**
 * Created by jastrelax on 2018/1/14.
 */
@BindLayout(value = R.layout.view_holder_order, dataTypes = Order.class)
public class OrderViewHolder extends BaseViewHolder<ViewHolderOrderBinding> {

    public OrderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBindView(T data, int position) {
        if (data == null) return;
        Order order = (Order) data;
        mBinding.setOrder(order);
        mBinding.executePendingBindings();
    }
}

package com.fafabtc.app.ui.viewholder;

import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ViewHolderOrderBinding;
import com.fafabtc.app.di.component.DaggerAppComponent;
import com.fafabtc.app.ui.base.BaseViewHolder;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.utils.RxUtils;
import com.fafabtc.data.data.repo.OrderRepo;
import com.fafabtc.data.model.entity.exchange.Order;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/14.
 */
@BindLayout(value = R.layout.view_holder_order, dataTypes = Order.class)
public class OrderViewHolder extends BaseViewHolder<ViewHolderOrderBinding> {

    @Inject
    OrderRepo orderRepo;

    public OrderViewHolder(View itemView) {
        super(itemView);
        DaggerAppComponent.builder()
                .appContext(itemView.getContext().getApplicationContext())
                .build()
                .viewHolderComponent()
                .inject(this);
    }

    @Override
    public <T> void onBindView(T data, final int position) {
        if (data == null) return;
        final Order order = (Order) data;
        mBinding.setOrder(order);
        mBinding.executePendingBindings();
        switch (order.getType()) {
            case BUY:
                mBinding.tvOrderType.setBackground(itemView.getResources().getDrawable(R.drawable.tag_order_buy));
                mBinding.tvOrderType.setText(R.string.order_type_buy);
                break;
            case SELL:
                mBinding.tvOrderType.setBackground(itemView.getResources().getDrawable(R.drawable.tag_order_sell));
                mBinding.tvOrderType.setText(R.string.order_type_sell);
                break;
        }

        switch (order.getState()) {
            case DONE:
                mBinding.btnOrderState.setEnabled(false);
                mBinding.btnOrderState.setText(R.string.order_state_done);
                break;
            case CANCELLED:
                mBinding.btnOrderState.setEnabled(false);
                mBinding.btnOrderState.setText(R.string.order_state_cancelled);
                break;
            case PENDING:
                mBinding.btnOrderState.setEnabled(true);
                mBinding.btnOrderState.setText(R.string.order_state_pending);
                mBinding.btnOrderState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // cancel order
                        cancelOrder(order, position);
                    }
                });
                break;

        }
    }

    private void cancelOrder(final Order order, final int position) {
        if (!mBinding.btnOrderState.isEnabled()) return;
        orderRepo.cancelOrder(order.getUuid())
                .compose(RxUtils.completableAsyncIO())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        order.setState(Order.State.CANCELLED);
                        adapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
}

package com.fafabtc.app.ui.viewholder;

import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ViewHolderExchangeEntryBinding;
import com.fafabtc.app.ui.activity.ExchangeAssetsActivity;
import com.fafabtc.app.ui.base.BaseViewHolder;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.data.model.vo.ExchangeEntry;

/**
 * Created by jastrelax on 2018/1/8.
 */

@BindLayout(value = R.layout.view_holder_exchange_entry, dataTypes = ExchangeEntry.class)
public class ExchangeEntryViewHolder extends BaseViewHolder<ViewHolderExchangeEntryBinding>{

    public ExchangeEntryViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBindView(T data, int position) {
        if (data == null)
            return;
        final ExchangeEntry entry = (ExchangeEntry) data;
        mBinding.tvExchangeName.setText(entry.getExchange().getName().toUpperCase());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeAssetsActivity.start(v.getContext(), entry.getAccountAssets(), entry.getExchange());
            }
        });
    }
}

package com.fafabtc.app.ui.viewholder;

import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ViewHolderBalanceAssetsBinding;
import com.fafabtc.app.ui.activity.BalanceDepositActivity;
import com.fafabtc.app.ui.base.BaseViewHolder;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;

/**
 * Created by jastrelax on 2018/1/9.
 */
@Deprecated
@BindLayout(value = R.layout.view_holder_balance_assets, dataTypes = BlockchainAssets.class)
public class BalanceAssetsViewHolder extends BaseViewHolder<ViewHolderBalanceAssetsBinding> {

    public BalanceAssetsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBindView(T data, int position) {
        if (data == null) return;
        final BlockchainAssets assets = (BlockchainAssets) data;
        mBinding.setBalanceAssets(assets);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BalanceDepositActivity.start(v.getContext(), assets);
            }
        });
    }
}

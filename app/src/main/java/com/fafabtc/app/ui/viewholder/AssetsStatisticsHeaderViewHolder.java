package com.fafabtc.app.ui.viewholder;

import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ViewHolderAssetsStatisticsHeaderBinding;
import com.fafabtc.app.ui.base.BaseViewHolder;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.data.model.vo.AssetsStatisticsHeader;

/**
 * Created by jastrelax on 2018/1/14.
 */
@BindLayout(value = R.layout.view_holder_assets_statistics_header, dataTypes = AssetsStatisticsHeader.class)
public class AssetsStatisticsHeaderViewHolder extends BaseViewHolder<ViewHolderAssetsStatisticsHeaderBinding>{

    public AssetsStatisticsHeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(Object data, int position) {
        if (data == null) return;
        AssetsStatisticsHeader header = (AssetsStatisticsHeader) data;
        mBinding.setData(header);
        mBinding.executePendingBindings();
    }
}

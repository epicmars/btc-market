package com.fafabtc.app.ui.viewholder;

import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ViewHolderAssetsStatisticsBinding;
import com.fafabtc.app.ui.base.BaseViewHolder;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.data.model.vo.AssetsStatistics;

/**
 * Created by jastrelax on 2018/1/11.
 */
@BindLayout(value = R.layout.view_holder_assets_statistics, dataTypes = AssetsStatistics.class)
public class AssetsStatisticsViewHolder extends BaseViewHolder<ViewHolderAssetsStatisticsBinding>{

    public AssetsStatisticsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public <T> void onBindView(T data, int position) {
        if (null == data) return;
        AssetsStatistics statistics = (AssetsStatistics) data;
        mBinding.setAssetsStatistics(statistics);
        mBinding.executePendingBindings();
    }
}

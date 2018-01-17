package com.fafabtc.app.di.component;

import com.fafabtc.app.ui.viewholder.AccountAssetsViewHolder;
import com.fafabtc.app.ui.viewholder.OrderViewHolder;

import dagger.Subcomponent;

/**
 * Created by jastrelax on 2018/1/12.
 */

@Subcomponent
public interface ViewHolderComponent {

    void inject(AccountAssetsViewHolder accountAssetsViewHolder);

    void inject(OrderViewHolder orderViewHolder);
}

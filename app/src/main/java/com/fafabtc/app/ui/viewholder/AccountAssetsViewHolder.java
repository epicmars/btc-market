package com.fafabtc.app.ui.viewholder;

import android.view.View;
import android.widget.CompoundButton;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ViewHolderAccountAssetsBinding;
import com.fafabtc.app.di.component.DaggerAppComponent;
import com.fafabtc.app.ui.activity.ExchangeEntryActivity;
import com.fafabtc.app.ui.base.BaseViewHolder;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.data.data.repo.AccountAssetsRepo;
import com.fafabtc.data.model.entity.exchange.AccountAssets;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/8.
 */

@BindLayout(value = R.layout.view_holder_account_assets, dataTypes = AccountAssets.class)
public class AccountAssetsViewHolder extends BaseViewHolder<ViewHolderAccountAssetsBinding> {

    @Inject
    AccountAssetsRepo accountAssetsRepo;

    public AccountAssetsViewHolder(View itemView) {
        super(itemView);
        DaggerAppComponent.builder()
                .appContext(itemView.getContext().getApplicationContext())
                .build()
                .viewHolderComponent()
                .inject(this);
    }

    @Override
    public <T> void onBindView(T data, int position) {
        if (data == null) return;
        final AccountAssets assets = (AccountAssets) data;
        mBinding.tvName.setText(assets.getName());
        mBinding.radioContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.radioChooser.toggle();
            }
        });
        mBinding.radioChooser.setChecked(assets.getState() == AccountAssets.State.CURRENT_ACTIVE ? true : false);

        mBinding.radioChooser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateListUI(assets);
                    accountAssetsRepo.getCurrent()
                            .flatMapCompletable(new Function<AccountAssets, CompletableSource>() {
                                @Override
                                public CompletableSource apply(AccountAssets currentAssets) throws Exception {
                                    currentAssets.setState(AccountAssets.State.ACTIVE);
                                    assets.setState(AccountAssets.State.CURRENT_ACTIVE);
                                    return accountAssetsRepo.update(currentAssets, assets);
                                }
                            })
                            .onErrorResumeNext(new Function<Throwable, CompletableSource>() {
                                @Override
                                public CompletableSource apply(Throwable throwable) throws Exception {
                                    assets.setState(AccountAssets.State.CURRENT_ACTIVE);
                                    return accountAssetsRepo.update(assets);
                                }
                            })
                            .subscribeOn(Schedulers.from(ExecutorManager.getNOW()))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onComplete() {
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Timber.e(e);
                                }
                            });
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeEntryActivity.start(v.getContext(), assets);
            }
        });
    }

    private void updateListUI(AccountAssets assets) {
        assets.setState(AccountAssets.State.CURRENT_ACTIVE);
        RecyclerAdapter recyclerAdapter = (RecyclerAdapter) adapter;
        List<Object> objects = recyclerAdapter.getPayloads();
        for (Object object : objects) {
            AccountAssets accountAssets = (AccountAssets) object;
            if (accountAssets.getState() == AccountAssets.State.CURRENT_ACTIVE
                    && !accountAssets.getUuid().equals(assets.getUuid())) {
                accountAssets.setState(AccountAssets.State.ACTIVE);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled() {
        super.onViewRecycled();
        mBinding.radioChooser.setOnCheckedChangeListener(null);
    }

    @Override
    protected void onDetachedToWindow() {
        super.onDetachedToWindow();
    }
}

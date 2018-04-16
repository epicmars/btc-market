package com.fafabtc.app.ui.viewholder;

import android.view.View;
import android.widget.CompoundButton;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ViewHolderPortfolioBinding;
import com.fafabtc.app.di.component.DaggerAppComponent;
import com.fafabtc.app.ui.activity.ExchangeEntryActivity;
import com.fafabtc.app.ui.base.BaseViewHolder;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.base.RecyclerAdapter;
import com.fafabtc.app.utils.ExecutorManager;
import com.fafabtc.data.data.repo.PortfolioRepo;
import com.fafabtc.data.model.entity.exchange.Portfolio;

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

@BindLayout(value = R.layout.view_holder_portfolio, dataTypes = Portfolio.class)
public class PortfolioViewHolder extends BaseViewHolder<ViewHolderPortfolioBinding> {

    @Inject
    PortfolioRepo portfolioRepo;

    public PortfolioViewHolder(View itemView) {
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
        final Portfolio assets = (Portfolio) data;
        mBinding.tvName.setText(assets.getName());
        mBinding.radioContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.radioChooser.toggle();
            }
        });
        mBinding.radioChooser.setChecked(assets.getState() == Portfolio.State.CURRENT_ACTIVE ? true : false);

        mBinding.radioChooser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateListUI(assets);
                    portfolioRepo.getCurrent()
                            .flatMapCompletable(new Function<Portfolio, CompletableSource>() {
                                @Override
                                public CompletableSource apply(Portfolio portfolio) throws Exception {
                                    portfolio.setState(Portfolio.State.ACTIVE);
                                    assets.setState(Portfolio.State.CURRENT_ACTIVE);
                                    return portfolioRepo.update(portfolio, assets);
                                }
                            })
                            .onErrorResumeNext(new Function<Throwable, CompletableSource>() {
                                @Override
                                public CompletableSource apply(Throwable throwable) throws Exception {
                                    assets.setState(Portfolio.State.CURRENT_ACTIVE);
                                    return portfolioRepo.update(assets);
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

    private void updateListUI(Portfolio assets) {
        assets.setState(Portfolio.State.CURRENT_ACTIVE);
        RecyclerAdapter recyclerAdapter = (RecyclerAdapter) adapter;
        List<Object> objects = recyclerAdapter.getPayloads();
        for (Object object : objects) {
            Portfolio portfolio = (Portfolio) object;
            if (portfolio.getState() == Portfolio.State.CURRENT_ACTIVE
                    && !portfolio.getUuid().equals(assets.getUuid())) {
                portfolio.setState(Portfolio.State.ACTIVE);
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

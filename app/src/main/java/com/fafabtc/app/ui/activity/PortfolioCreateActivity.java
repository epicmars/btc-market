package com.fafabtc.app.ui.activity;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.app.databinding.ActivityPortfolioCreateBinding;
import com.fafabtc.app.ui.base.BaseActivity;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.fragment.LoadingDialog;
import com.fafabtc.app.vm.PortfolioCreateViewModel;
import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.base.model.Resource;

@BindLayout(R.layout.activity_portfolio_create)
public class PortfolioCreateActivity extends BaseActivity<ActivityPortfolioCreateBinding> {

    private PortfolioCreateViewModel mViewModel;
    private LoadingDialog loadingDialog;

    public static void start(Context context) {
        Intent starter = new Intent(context, PortfolioCreateActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel(PortfolioCreateViewModel.class);
        mViewModel.getPortfolio().observe(this, portfolioCreationObserver);
        initViews();
    }

    private void initViews() {
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String portfolioName = binding.etPortfolioName.getText().toString();
                if (TextUtils.isEmpty(portfolioName)) {
                    return;
                }
                mViewModel.createPortfolio(portfolioName);
                showLoadingDialog();
            }
        });
    }

    private void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.newInstance();
            loadingDialog.show(getSupportFragmentManager(), null);
        }
    }

    private void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismissAllowingStateLoss();
        }
    }

    private Observer<Resource<Portfolio>> portfolioCreationObserver = new Observer<Resource<Portfolio>>() {
        @Override
        public void onChanged(@Nullable Resource<Portfolio> portfolioResource) {
            dismissLoadingDialog();
            if (portfolioResource != null && portfolioResource.isSuccess()) {
                sendBroadcast(new Intent(Broadcasts.Actions.ACTION_ASSETS_CREATED));
                finish();
                ExchangeEntryActivity.start(PortfolioCreateActivity.this, portfolioResource.data);
            } else {
                String message = portfolioResource != null ? portfolioResource.message : "资产初始化中";
                Toast.makeText(PortfolioCreateActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    };
}

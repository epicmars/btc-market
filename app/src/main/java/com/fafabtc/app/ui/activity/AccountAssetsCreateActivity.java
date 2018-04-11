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
import com.fafabtc.app.databinding.ActivityAccountAssetsCreateBinding;
import com.fafabtc.app.ui.base.BaseActivity;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.fragment.LoadingDialog;
import com.fafabtc.app.vm.AccountAssetsCreateViewModel;
import com.fafabtc.data.model.entity.exchange.AccountAssets;
import com.fafabtc.domain.model.Resource;

@BindLayout(R.layout.activity_account_assets_create)
public class AccountAssetsCreateActivity extends BaseActivity<ActivityAccountAssetsCreateBinding> {

    private AccountAssetsCreateViewModel mViewModel;
    private LoadingDialog loadingDialog;

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountAssetsCreateActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel(AccountAssetsCreateViewModel.class);
        mViewModel.getAccountAssets().observe(this, accountAssetsCreationObserver);
        initViews();
    }

    private void initViews() {
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String assetsName = binding.etAccountAssetsName.getText().toString();
                if (TextUtils.isEmpty(assetsName)) {
                    return;
                }
                mViewModel.createAccountAssets(assetsName);
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

    private Observer<Resource<AccountAssets>> accountAssetsCreationObserver = new Observer<Resource<AccountAssets>>() {
        @Override
        public void onChanged(@Nullable Resource<AccountAssets> accountAssets) {
            dismissLoadingDialog();
            if (accountAssets != null && accountAssets.isSuccess()) {
                sendBroadcast(new Intent(Broadcasts.Actions.ACTION_ASSETS_CREATED));
                finish();
                ExchangeEntryActivity.start(AccountAssetsCreateActivity.this, accountAssets.data);
            } else {
                String message = accountAssets != null ? accountAssets.message : "资产初始化中";
                Toast.makeText(AccountAssetsCreateActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    };
}

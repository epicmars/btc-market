package com.fafabtc.app.ui.activity;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ActivityAccountAssetsCreateBinding;
import com.fafabtc.app.ui.base.BaseActivity;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.vm.AccountAssetsCreateViewModel;
import com.fafabtc.data.model.entity.exchange.AccountAssets;

@BindLayout(R.layout.activity_account_assets_create)
public class AccountAssetsCreateActivity extends BaseActivity<ActivityAccountAssetsCreateBinding> {

    private AccountAssetsCreateViewModel mViewModel;

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
//                    Toast.makeText(AccountAssetsCreateActivity.this, "资产组合名为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mViewModel.createAccountAssets(assetsName);
            }
        });
    }

    private Observer<AccountAssets> accountAssetsCreationObserver = new Observer<AccountAssets>() {
        @Override
        public void onChanged(@Nullable AccountAssets accountAssets) {
            ExchangeEntryActivity.start(AccountAssetsCreateActivity.this, accountAssets);
        }
    };
}

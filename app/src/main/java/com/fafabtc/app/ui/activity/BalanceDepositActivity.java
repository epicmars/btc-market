package com.fafabtc.app.ui.activity;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.app.databinding.ActivityBalanceDepositBinding;
import com.fafabtc.app.ui.base.BaseActivity;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.fragment.LoadingDialog;
import com.fafabtc.app.vm.BalanceDepositViewModel;
import com.fafabtc.data.model.entity.exchange.BlockchainAssets;
import com.fafabtc.domain.model.Resource;

/**
 * Balance assets deposit through exchange.
 */
@BindLayout(R.layout.activity_balance_deposit)
public class BalanceDepositActivity extends BaseActivity<ActivityBalanceDepositBinding> {

    private static final String EXTRA_BALANCE_ASSETS = "BalanceDepositActivity.EXTRA_BALANCE_ASSETS";

    private BlockchainAssets blockchainAssets;

    private LoadingDialog loadingDialog;

    private BalanceDepositViewModel viewModel;

    public static void start(Context context, BlockchainAssets balanceAssets) {
        Intent starter = new Intent(context, BalanceDepositActivity.class);
        starter.putExtra(EXTRA_BALANCE_ASSETS, balanceAssets);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            blockchainAssets = intent.getParcelableExtra(EXTRA_BALANCE_ASSETS);
            setTitle(getString(R.string.assets_deposit_title_format, blockchainAssets.getName().toUpperCase()));
        }
        viewModel = getViewModel(BalanceDepositViewModel.class);
        viewModel.setBlockchainAssets(blockchainAssets);
        viewModel.getIsDepositSucceed().observe(this, depositResultObserver);

        binding.etBalance.setHint(getString(R.string.hint_balance_deposit, blockchainAssets.getName().toUpperCase()));
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String balanceNumStr = binding.etBalance.getText().toString();
                if (TextUtils.isEmpty(balanceNumStr)) {
//                    Toast.makeText(BalanceDepositActivity.this, "输入数量为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                double balance = 0;
                try {
                    balance = Double.parseDouble(balanceNumStr);
                } catch (NumberFormatException e) {
//                    Toast.makeText(BalanceDepositActivity.this, "无效的输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (balance <= 0) {
//                    Toast.makeText(BalanceDepositActivity.this, "无效的输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.depositBalance(balance);
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

    private Observer<Resource<Boolean>> depositResultObserver = new Observer<Resource<Boolean>>() {
        @Override
        public void onChanged(@Nullable Resource<Boolean> booleanResource) {
            dismissLoadingDialog();
            if (booleanResource != null && booleanResource.isSuccess()) {
                finish();
                sendBroadcast(new Intent(Broadcasts.Actions.ACTION_BALANCE_DEPOSITED));
            }
        }
    };
}

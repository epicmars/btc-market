package com.fafabtc.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.fafabtc.app.R;
import com.fafabtc.app.databinding.ActivityExchangeEntryBinding;
import com.fafabtc.app.di.Injectable;
import com.fafabtc.app.ui.base.BaseActivity;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.fragment.ExchangeEntryFragment;
import com.fafabtc.data.model.entity.exchange.AccountAssets;

@Injectable
@BindLayout(R.layout.activity_exchange_entry)
public class ExchangeEntryActivity extends BaseActivity<ActivityExchangeEntryBinding> {

    private static final String EXTRA_ACCOUNT_ASSETS = "ExchangeEntryActivity.EXTRA_ACCOUNT_ASSETS";

    public static void start(Context context, AccountAssets assets) {
        Intent starter = new Intent(context, ExchangeEntryActivity.class);
        starter.putExtra(EXTRA_ACCOUNT_ASSETS, assets);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AccountAssets accountAssets = null;
        Intent intent = getIntent();
        if (intent != null) {
            accountAssets = intent.getParcelableExtra(EXTRA_ACCOUNT_ASSETS);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, ExchangeEntryFragment.newInstance(accountAssets));
        ft.commitAllowingStateLoss();
    }
}

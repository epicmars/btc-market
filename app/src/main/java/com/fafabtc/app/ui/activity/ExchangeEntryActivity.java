package com.fafabtc.app.ui.activity;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fafabtc.app.R;
import com.fafabtc.app.constants.Broadcasts;
import com.fafabtc.app.databinding.ActivityExchangeEntryBinding;
import com.fafabtc.app.di.Injectable;
import com.fafabtc.app.ui.base.BaseActivity;
import com.fafabtc.app.ui.base.BindLayout;
import com.fafabtc.app.ui.fragment.ExchangeEntryFragment;
import com.fafabtc.app.vm.ExchangeEntryViewModel;
import com.fafabtc.data.model.entity.exchange.Portfolio;
import com.fafabtc.domain.model.Resource;

@Injectable
@BindLayout(R.layout.activity_exchange_entry)
public class ExchangeEntryActivity extends BaseActivity<ActivityExchangeEntryBinding> {

    private static final String EXTRA_PORTFOLIO = "ExchangeEntryActivity.EXTRA_PORTFOLIO";

    private ExchangeEntryViewModel viewModel;

    public static void start(Context context, Portfolio assets) {
        Intent starter = new Intent(context, ExchangeEntryActivity.class);
        starter.putExtra(EXTRA_PORTFOLIO, assets);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Portfolio portfolio = null;
        Intent intent = getIntent();
        if (intent != null) {
            portfolio = intent.getParcelableExtra(EXTRA_PORTFOLIO);
            setTitle(getString(R.string.exchange_entry_title_format, portfolio.getName()));
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, ExchangeEntryFragment.newInstance(portfolio));
        ft.commitAllowingStateLoss();

        viewModel = getViewModel(ExchangeEntryViewModel.class);
        viewModel.setPortfolio(portfolio);
        viewModel.getDeleteResult().observe(this, deleteResultObserver);
    }

    private Observer<Resource> deleteResultObserver = new Observer<Resource>() {
        @Override
        public void onChanged(@Nullable Resource result) {
            if (result == null) return;
            if (result.isSuccess()) {
                finish();
                sendBroadcast(new Intent(Broadcasts.Actions.ACTION_ASSETS_DELETED));
                Toast.makeText(ExchangeEntryActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ExchangeEntryActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exchange_entry, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_delete_portfolio:
                viewModel.deleteAssets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

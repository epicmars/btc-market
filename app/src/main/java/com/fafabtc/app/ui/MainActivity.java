package com.fafabtc.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fafabtc.app.R;
import com.fafabtc.app.di.Injectable;
import com.fafabtc.app.service.MainService;
import com.fafabtc.app.service.SyncService;
import com.fafabtc.app.service.TradeService;
import com.fafabtc.app.ui.fragment.SplashScreenFragment;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by jastrelax on 2018/1/6.
 */
@Injectable
public class MainActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SplashScreenFragment.newInstance().show(getSupportFragmentManager(), null);

        MainService.start(this);
        TradeService.start(this);
        SyncService.start(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

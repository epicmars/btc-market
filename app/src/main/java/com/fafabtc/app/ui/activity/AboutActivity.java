package com.fafabtc.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fafabtc.app.BuildConfig;
import com.fafabtc.app.R;

public class AboutActivity extends ThemeActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView btnFeedback = findViewById(R.id.btn_user_feed_back);
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackActivity.start(AboutActivity.this);
            }
        });

        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText(BuildConfig.VERSION_NAME);

    }
}

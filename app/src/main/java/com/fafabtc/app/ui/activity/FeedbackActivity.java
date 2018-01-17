package com.fafabtc.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fafabtc.app.R;
import com.fafabtc.app.ui.fragment.LoadingDialog;
import com.fafabtc.common.analysis.AnalysisHelper;
import com.fafabtc.common.file.SharedPreferenceUtils;
import com.fafabtc.common.json.GsonHelper;

import java.util.Date;

public class FeedbackActivity extends ThemeActivity {

    private static final String PREF_FEEDBACK = "feedback";
    private static final long TOAST_DELAY = 1000;
    private static final int MAX_FEEDBACK = 6;

    private Feedback feedback;
    private boolean isSending = false;
    private Handler handler = new Handler();
    private LoadingDialog dialog;

    public static void start(Context context) {
        Intent starter = new Intent(context, FeedbackActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        final EditText editText = findViewById(R.id.et_feedback);
        Button button = findViewById(R.id.btn_confirm);
        dialog = LoadingDialog.newInstance();

        final SharedPreferences pref = SharedPreferenceUtils.getPreference(this, PREF_FEEDBACK);
        try {
            feedback = GsonHelper.gson().fromJson(pref.getString(Feedback.class.getCanonicalName(), null), Feedback.class);
        } catch (Exception e) {

        }
        final Date now = new Date();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackString = editText.getText().toString().trim();
                if (TextUtils.isEmpty(feedbackString)) return;
                if (isSending) {
                    return;
                }
                isSending = true;
                dialog.show(getSupportFragmentManager(), null);

                if (feedback != null && now.getTime() - feedback.timestamp <= 2 * 3600 * 1000 && feedback.count >= MAX_FEEDBACK) {
                    toastSuccess(TOAST_DELAY);
                } else {
                    if (feedback == null) feedback = new Feedback();
                    if (feedback.count >= MAX_FEEDBACK) feedback.count = 0;
                    feedback.count++;
                    feedback.timestamp = now.getTime();
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Feedback.class.getCanonicalName(), GsonHelper.gson().toJson(feedback));
                    editor.apply();
                    AnalysisHelper.sendFeedback(FeedbackActivity.this, feedbackString, new Runnable() {
                        @Override
                        public void run() {
                            toastSuccess(0);
                        }
                    });
                }
            }
        });
    }

    private void toastSuccess(long delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) dialog.dismissAllowingStateLoss();
                Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, delay);
    }

    public static class Feedback {
        public long timestamp;
        public int count;
    }
}

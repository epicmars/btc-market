package com.fafabtc.app.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fafabtc.app.R;

/**
 * Created by jastrelax on 2018/1/12.
 */

public class LabelValueView extends LinearLayout {

    private TextView mTextLabel;
    private TextView mTextValue;
    private String mLabel;
    private String mValue;

    public LabelValueView(Context context) {
        this(context, null);
    }

    public LabelValueView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelValueView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
        inflate(context, R.layout.view_label_value, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelValueView);
        mLabel = a.getString(R.styleable.LabelValueView_label);
        mValue = a.getString(R.styleable.LabelValueView_value);
        a.recycle();

        mTextLabel = findViewById(R.id.text_label);
        mTextValue = findViewById(R.id.text_value);
        setLabel(mLabel);
        setValue(mValue);
    }

    public void setLabel(String name) {
        mTextLabel.setText(name);
    }

    public void setValue(String amount) {
        mTextValue.setText(amount);
    }

}

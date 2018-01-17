package com.fafabtc.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fafabtc.app.R;

/**
 * Created by jastrelax on 2018/1/17.
 */

public class LoadingDialog extends DialogFragment {

    public static LoadingDialog newInstance() {
        Bundle args = new Bundle();

        LoadingDialog fragment = new LoadingDialog();
        fragment.setArguments(args);
        return fragment;
    }

    {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_loading, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

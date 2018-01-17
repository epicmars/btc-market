package com.fafabtc.app.ui.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fafabtc.app.vm.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatDialogFragment;

/**
 * Created by jastrelax on 2018/1/17.
 */

public abstract class BaseDialogFragment<VDB extends ViewDataBinding> extends DaggerAppCompatDialogFragment {

    protected VDB binding;

    protected BindLayout mBindLayout;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mBindLayout = getClass().getAnnotation(BindLayout.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mBindLayout != null && mBindLayout.value() != 0) {
            binding = DataBindingUtil.inflate(inflater, mBindLayout.value(), container, false);
            return binding.getRoot();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (binding != null) {
            binding.unbind();
        }
    }

    protected <T extends ViewModel> T getViewModelOfActivity(Class<T> viewModelClass) {
        return ViewModelProviders.of(this.getActivity(), viewModelFactory).get(viewModelClass);
    }

    protected <T extends ViewModel> T getViewModel(Class<T> viewModelClass) {
        return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass);
    }
}

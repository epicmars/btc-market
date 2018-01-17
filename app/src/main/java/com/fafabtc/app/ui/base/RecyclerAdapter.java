package com.fafabtc.app.ui.base;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 列表适配器，一个列表适配的列表项展示什么内容只与BaseViewHolder的实现类有关。
 *
 * 一个BaseViewHolder类包含了所需要的布局信息，数据类型信息，RecyclerAdapter负责将负载数据项
 * 映射到一种BaseViewHolder的子类。
 *
 * Created by jastrelax on 2017/8/30.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private final SparseIntArray mDataViewMap = new SparseIntArray();
    private final SparseArray<Class<? extends BaseViewHolder>> mViewHolderMap= new SparseArray<>();
    private final List<Object> mPayloads = new ArrayList<>();
    private FragmentManager mFragmentManager;

    /**
     * 注册一个或多个BaseViewHolder以用于数据展示。
     * @param clazzArray
     * @return
     */
    public RecyclerAdapter register(Class<? extends BaseViewHolder>... clazzArray) {

        for (Class clazz : clazzArray) {
            BindLayout bindLayout = (BindLayout) clazz.getAnnotation(BindLayout.class);
            // 建立数据类型到布局的映射
            for (Class dataType : bindLayout.dataTypes()) {
                mDataViewMap.append(dataType.hashCode(), bindLayout.value());
            }
            // 建立布局到BaseViewHolder的映射
            mViewHolderMap.put(bindLayout.value(), clazz);
        }
        return this;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<? extends BaseViewHolder> viewHolderClass = mViewHolderMap.get(viewType);
        // If viewHolderClass is null, the ViewHolder may not be registered
        return BaseViewHolder.instance(viewHolderClass, parent, mFragmentManager, this);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Object item = mPayloads.get(position);
        holder.onBindInternal(item, position);
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onDetachedToWindow();
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    @Override
    public int getItemCount() {
        return mPayloads.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mPayloads.get(position);
        return mDataViewMap.get(item.getClass().hashCode());
    }

    public List<Object> getPayloads() {
        return mPayloads;
    }

    public void setFragmentManager(FragmentManager mFragmentManager) {
        this.mFragmentManager = mFragmentManager;
    }

    /**
     * Set adapter payloads.
     * @param payloads
     */
    public <T> void setPayloads(T... payloads) {
        setPayloads(Arrays.asList(payloads));
    }

    /**
     * Set adapter payloads.
     * @param payloads
     */
    public void setPayloads(Collection<?> payloads) {
        if (null == payloads) {
            return;
        }
        this.mPayloads.clear();
        this.mPayloads.addAll(payloads);
        notifyDataSetChanged();
    }

    public void updatePayloads(Collection<?> payloads) {
        if (payloads == null) {
            return;
        }
        if (mPayloads.size() != payloads.size()) {
            this.mPayloads.clear();
            this.mPayloads.addAll(payloads);
            notifyItemRangeChanged(0, payloads.size());
        } else {
            this.mPayloads.clear();
            this.mPayloads.addAll(payloads);
            notifyDataSetChanged();
        }
    }

    /**
     * Add payload to current payloads.
     * @param payloads
     */
    public void addPayloads(Collection<?> payloads) {
        if (null == payloads || payloads.isEmpty()) {
            return;
        }
        int positionStart = mPayloads.size();
        int itemCount = payloads.size();
        this.mPayloads.addAll(payloads);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    /**
     * Append payloads with source, the source doesn't contain the payloads to be appended.
     *
     * @param source   source payloads
     * @param payloads payloads to be appended
     */
    public void appendPayloads(Collection<?> source, Collection<?> payloads) {
        // Adapter payload is empty, set to source.
        if (null == payloads || payloads.isEmpty()) {
            setPayloads(source);
            return;
        }
        if (!mPayloads.isEmpty() || source == null || source.isEmpty()) {
            // The source is empty, add to current payloads.
            addPayloads(payloads);
            return;
        }
        int positionStart = source.size();
        int itemCount = payloads.size();
        mPayloads.addAll(source);
        mPayloads.addAll(payloads);
        notifyItemRangeInserted(positionStart, itemCount);
    }
}

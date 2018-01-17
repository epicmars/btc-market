package com.fafabtc.app.ui.base;

import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jastrelax on 2018/1/21.
 */

public class BaseListAdapter extends BaseAdapter {

    private final SparseIntArray mDataViewMap = new SparseIntArray();
    private final SparseArray<Class<? extends BaseViewHolder>> mViewHolderMap= new SparseArray<>();
    private final List<Object> payloads = new ArrayList<>();
    private FragmentManager fragmentManager;

    /**
     * 注册一个或多个BaseViewHolder以用于数据展示。
     * @param clazzArray
     * @return
     */
    public BaseListAdapter register(Class<? extends BaseViewHolder>... clazzArray) {

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
    public int getCount() {
        return payloads.size();
    }

    @Override
    public Object getItem(int position) {
        return payloads.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = payloads.get(position);
        int layoutRes = mDataViewMap.get(item.getClass().hashCode());
        Class<? extends BaseViewHolder> viewHolderClass = mViewHolderMap.get(layoutRes);
        if (convertView == null || convertView.getTag(layoutRes) == null) {
            BaseViewHolder viewHolder = BaseViewHolder.instance(viewHolderClass, parent);
            viewHolder.onBindInternal(item, position);
            convertView  = viewHolder.itemView;
            convertView.setTag(layoutRes, viewHolder);
        } else {
            BaseViewHolder viewHolder = (BaseViewHolder) convertView.getTag(layoutRes);
            viewHolder.onBindInternal(item, position);
            convertView  = viewHolder.itemView;
        }
        return convertView;
    }

    public void setPayloads(Collection<?> payloads) {
        if (payloads == null) return;
        this.payloads.clear();
        this.payloads.addAll(payloads);
        notifyDataSetChanged();
    }
}

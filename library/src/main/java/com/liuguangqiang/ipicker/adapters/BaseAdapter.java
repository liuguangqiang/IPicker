/*
 *  Copyright 2015 GoIn Inc. All rights reserved.
 */

package com.liuguangqiang.ipicker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 15/7/7.
 */
public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public Context mContext;
    public List<T> data = new ArrayList<>();
    public final LayoutInflater layoutInflater;

    public OnItemClickListener onItemClickListener;
    public OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public BaseAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (holder.itemView != null) {
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v, position);
                    }
                });
            }

            if (onItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemLongClickListener.onItemLongClick(position);
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

    public interface OnItemLongClickListener {

        void onItemLongClick(int position);

    }

}

package com.liuguangqiang.ipicker.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liuguangqiang.ipicker.adapters.BaseAdapter;
import com.liuguangqiang.ipicker.internal.Logger;

import java.util.List;

/**
 * Created by Eric on 16/9/12.
 */
public class SelectedAdapter extends BaseAdapter<String, SelectedAdapter.ViewHolder> {

    public SelectedAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bindData(data.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;

        public ViewHolder(LayoutInflater layoutInflater, ViewGroup parent, boolean attachToRoot) {
            super(layoutInflater.inflate(R.layout.item_selected, parent, attachToRoot));
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
        }

        public void bindData(String path) {
            Glide.with(itemView.getContext()).load(path).into(ivPhoto);
        }

    }

}

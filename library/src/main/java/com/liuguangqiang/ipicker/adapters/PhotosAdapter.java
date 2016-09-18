package com.liuguangqiang.ipicker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liuguangqiang.ipicker.R;
import com.liuguangqiang.ipicker.entities.Photo;
import com.liuguangqiang.ipicker.internal.Logger;

import java.util.List;


/**
 * Created by Eric on 16/9/12.
 */
public class PhotosAdapter extends BaseAdapter<Photo, PhotosAdapter.ViewHolder> {

    public PhotosAdapter(Context context, List<Photo> data) {
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
        private ImageView ivCamera;

        public ViewHolder(LayoutInflater layoutInflater, ViewGroup parent, boolean attachToRoot) {
            super(layoutInflater.inflate(R.layout.item_photo, parent, attachToRoot));
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            ivCamera = (ImageView) itemView.findViewById(R.id.iv_camera);
        }

        public void bindData(Photo entity) {
            if (entity.showCamera) {
                ivPhoto.setVisibility(View.GONE);
                ivCamera.setVisibility(View.VISIBLE);
            } else {
                ivPhoto.setVisibility(View.VISIBLE);
                ivCamera.setVisibility(View.GONE);
                Glide.with(itemView.getContext()).load(entity.path).into(ivPhoto);
            }
        }
    }

}

/*
 * Copyright 2016 Eric Liu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liuguangqiang.ipicker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liuguangqiang.ipicker.R;
import com.liuguangqiang.ipicker.entities.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 16/9/12.
 */
public class PhotosAdapter extends BaseAdapter<Photo, PhotosAdapter.ViewHolder> {

    private List<String> selected = new ArrayList<>();
    private boolean isSingleSelection = true;

    public PhotosAdapter(Context context, List<Photo> data) {
        super(context, data);
    }

    public void setSingleSelection(boolean singleSelection) {
        this.isSingleSelection = singleSelection;
    }

    public void addSelected(List<String> list) {
        selected.addAll(list);
    }

    public void addSelected(Photo photo) {
        selected.add(photo.path);
    }

    public void removeSelected(Photo photo) {
        selected.remove(photo.path);
    }

    public List<String> getSelected() {
        return selected;
    }

    public boolean isSelected(String path) {
        return selected.contains(path);
    }

    public int getSelectedTotal() {
        return selected.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater, parent, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bindData(data.get(position), !isSingleSelection && isSelected(data.get(position).path));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPhoto;
        private ImageView ivCamera;
        private ImageView ivCheckbox;

        public ViewHolder(LayoutInflater layoutInflater, ViewGroup parent, boolean attachToRoot) {
            super(layoutInflater.inflate(R.layout.item_photo, parent, attachToRoot));
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            ivCamera = (ImageView) itemView.findViewById(R.id.iv_camera);
            ivCheckbox = (ImageView) itemView.findViewById(R.id.iv_checkbox);
        }

        public void bindData(Photo entity, boolean selected) {
            if (entity.showCamera) {
                ivPhoto.setVisibility(View.GONE);
                ivCamera.setVisibility(View.VISIBLE);
                ivCheckbox.setVisibility(View.GONE);
            } else {
                ivPhoto.setVisibility(View.VISIBLE);
                ivCamera.setVisibility(View.GONE);
                ivCheckbox.setVisibility(selected ? View.VISIBLE : View.GONE);
                Glide.with(itemView.getContext()).load(entity.path).into(ivPhoto);
            }
        }

    }

}

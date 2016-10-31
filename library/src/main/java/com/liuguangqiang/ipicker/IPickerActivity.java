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

package com.liuguangqiang.ipicker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.liuguangqiang.ipicker.adapters.BaseAdapter;
import com.liuguangqiang.ipicker.adapters.PhotosAdapter;
import com.liuguangqiang.ipicker.crop.Crop;
import com.liuguangqiang.ipicker.entities.Photo;
import com.liuguangqiang.ipicker.internal.ImageMedia;
import com.liuguangqiang.permissionhelper.PermissionHelper;
import com.liuguangqiang.permissionhelper.annotations.PermissionDenied;
import com.liuguangqiang.permissionhelper.annotations.PermissionGranted;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 16/9/12.
 */
public class IPickerActivity extends AppCompatActivity implements BaseAdapter.OnItemClickListener {

    public static final String ARG_SELECTED = "ARG_SELECTED";
    public static final String ARG_LIMIT = "ARG_LIMIT";
    public static final String ARG_CROP_ENABLE = "ARG_CROP_ENABLE";

    private static final int REQUEST_CAMERA = 1;
    private static final int ACTION_DONE = 0;

    private RelativeLayout layoutContainer;
    private RecyclerView recyclerView;
    private PhotosAdapter adapter;
    private List<Photo> photoList = new ArrayList<>();
    private int limit = 1;
    private boolean cropEnable = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipicker);
        initToolbar();
        initViews();
        getArguments();
        requestPhotos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case ACTION_DONE:
                IPicker.finish(adapter.getSelected());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ACTION_DONE, 0, R.string.action_done).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(ACTION_DONE).setVisible(limit > 1 ? true : false);
        return super.onPrepareOptionsMenu(menu);
    }

    private void initToolbar() {
        setTitle(R.string.title_act_picker);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_close_white);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initViews() {
        getWindow().setBackgroundDrawable(null);
        recyclerView = (RecyclerView) findViewById(R.id.rv_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4, GridLayoutManager.VERTICAL, false));
        adapter = new PhotosAdapter(this, photoList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        layoutContainer = (RelativeLayout) findViewById(R.id.layout_container);
    }

    private void getArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(ARG_LIMIT)) {
                limit = bundle.getInt(ARG_LIMIT);
                if (limit == 0) limit = 1;

                adapter.setSingleSelection(isSingleSelection());
            }

            if (bundle.containsKey(ARG_SELECTED)) {
                adapter.addSelected(bundle.getStringArrayList(ARG_SELECTED));
                updateTitle();
            }

            if (bundle.containsKey(ARG_CROP_ENABLE)) {
                cropEnable = bundle.getBoolean(ARG_CROP_ENABLE, false);
            }
        }
    }

    private boolean isSingleSelection() {
        return limit == 1;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == 0) {
            requestCamera();
        } else if (isSingleSelection()) {
            if (cropEnable) {
                cropImage(Uri.parse("file://" + photoList.get(position).path));
            } else {
                IPicker.finish(photoList.get(position).path);
                finish();
            }
        } else if (adapter.isSelected(photoList.get(position).path)) {
            removeSelected(position);
        } else {
            addSelected(position);
        }
    }

    private void removeSelected(int position) {
        adapter.removeSelected(photoList.get(position));
        adapter.notifyItemChanged(position);
        updateTitle();
    }

    private void addSelected(int position) {
        if ((adapter.getSelectedTotal() < limit)) {
            adapter.addSelected(photoList.get(position));
            adapter.notifyItemChanged(position);
            updateTitle();
        } else {
            Snackbar.make(layoutContainer, getString(R.string.format_max_size, limit), Snackbar.LENGTH_LONG).show();
        }
    }

    private void updateTitle() {
        if (limit > 1) {
            if (adapter.getSelectedTotal() == 0) {
                setTitle(R.string.title_act_picker);
            } else {
                setTitle("" + adapter.getSelectedTotal());
            }
        }
    }

    private void requestPhotos() {
        PermissionHelper.getInstance().requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void requestCamera() {
        PermissionHelper.getInstance().requestPermission(this, Manifest.permission.CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.getInstance().onRequestPermissionsResult(this, permissions, grantResults);
    }

    @PermissionGranted(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    public void galleryGranted() {
        getPhotos();
    }

    @PermissionDenied(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    public void galleryDenied() {
        promptNoPermission(R.string.no_permission_gallery);
    }

    @PermissionGranted(permission = Manifest.permission.CAMERA)
    public void cameraGranted() {
        takePicture();
    }

    @PermissionDenied(permission = Manifest.permission.CAMERA)
    public void cameraDenied() {
        promptNoPermission(R.string.no_permission_camera);
    }

    private void getPhotos() {
        photoList.add(new Photo(true));
        photoList.addAll(ImageMedia.queryAll(this));
        adapter.notifyDataSetChanged();
    }

    private Uri tempUri;

    private void deleteTemp() {
        if (tempUri != null) {
            getContentResolver().delete(tempUri, null, null);
        }
    }

    private void takePicture() {
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        tempUri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (tempUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        }
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    if (isSingleSelection()) {
                        if (cropEnable) {
                            cropImage(tempUri);
                        } else {
                            IPicker.finish(ImageMedia.getFilePath(getApplicationContext(), tempUri));
                            finish();
                        }
                    } else {
                        String path = ImageMedia.getFilePath(getApplicationContext(), tempUri);
                        Photo photo = new Photo(path);
                        photoList.add(1, photo);
                        if (adapter.getSelectedTotal() < limit) {
                            adapter.addSelected(photo);
                        }
                        adapter.notifyDataSetChanged();
                        updateTitle();
                    }
                    break;
                case Crop.REQUEST_CROP:
                    IPicker.finish(Crop.getOutput(data).toString());
                    finish();
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    deleteTemp();
                    break;
            }
        }
    }

    private void promptNoPermission(@StringRes int res) {
        Snackbar.make(layoutContainer, res, Snackbar.LENGTH_LONG).setAction(R.string.btn_setting, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }).show();
    }

    private void cropImage(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped" + System.currentTimeMillis()));
        Crop.of(source, destination).asSquare().start(this);
    }

}

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.liuguangqiang.ipicker.adapters.BaseAdapter;
import com.liuguangqiang.ipicker.adapters.PhotosAdapter;
import com.liuguangqiang.ipicker.entities.Photo;
import com.liuguangqiang.ipicker.internal.ImageMedia;
import com.liuguangqiang.permissionhelper.PermissionHelper;
import com.liuguangqiang.permissionhelper.annotations.PermissionDenied;
import com.liuguangqiang.permissionhelper.annotations.PermissionGranted;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 16/9/12.
 */
public class IPickerActivity extends AppCompatActivity implements BaseAdapter.OnItemClickListener, View.OnClickListener {

    public static final String ARG_SELECTED = "ARG_SELECTED";

    public static final String ARG_LIMIT = "ARG_LIMIT";

    private static final int REQUEST_CAMERA = 1;

    private RecyclerView recyclerView;
    private FloatingActionButton btnSelect;
    private PhotosAdapter adapter;
    private List<Photo> photoList = new ArrayList<>();
    private int limit = 1;

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setTitle(R.string.title_act_picker);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4, GridLayoutManager.VERTICAL, false));

        adapter = new PhotosAdapter(this, photoList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        btnSelect = (FloatingActionButton) findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(this);
    }

    private void getArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(ARG_LIMIT)) {
                limit = bundle.getInt(ARG_LIMIT);
                if (limit == 0) limit = 1;

                adapter.setSingleSelection(isSingleSelection());
                btnSelect.setVisibility(limit > 1 ? View.VISIBLE : View.GONE);
            }

            if (bundle.containsKey(ARG_SELECTED)) {
                adapter.addSelected(bundle.getStringArrayList(ARG_SELECTED));
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
            IPicker.finish(photoList.get(position).path);
            finish();
        } else if (adapter.isSelected(photoList.get(position).path)) {
            removeSeleted(position);
        } else {
            addSeleted(position);
        }
    }

    private void removeSeleted(int position) {
        adapter.removeSelected(photoList.get(position));
        adapter.notifyItemChanged(position);
    }

    private void addSeleted(int position) {
        if ((adapter.getSelectedTotal() < limit)) {
            adapter.addSelected(photoList.get(position));
            adapter.notifyItemChanged(position);
        } else {
            Snackbar.make(recyclerView, getString(R.string.format_max_size, limit), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_select) {
            IPicker.finish(adapter.getSelected());
            finish();
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
                        IPicker.finish(tempUri.toString());
                        finish();
                    } else {
                        Photo photo = new Photo(tempUri.toString());
                        photoList.add(1, photo);
                        adapter.addSelected(photo);
                        adapter.notifyDataSetChanged();
                    }
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
        Snackbar.make(recyclerView, res, Snackbar.LENGTH_LONG).setAction(R.string.btn_setting, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }).show();
    }

}

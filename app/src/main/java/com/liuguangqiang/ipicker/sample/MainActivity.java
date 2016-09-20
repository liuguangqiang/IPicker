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

package com.liuguangqiang.ipicker.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.liuguangqiang.ipicker.IPicker;
import com.liuguangqiang.ipicker.events.IPickerEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * A Sample
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SelectedAdapter adapter;
    private ArrayList<String> selectPictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEvent(IPickerEvent event) {
        selectPictures.clear();
        selectPictures.addAll(event.selected);
        adapter.notifyDataSetChanged();
    }

    private void initViews() {
        IPicker.setLimit(5);
        Button button = (Button) findViewById(R.id.open_picker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IPicker.open(getApplicationContext(), selectPictures);
            }
        });
        adapter = new SelectedAdapter(getApplicationContext(), selectPictures);
        recyclerView = (RecyclerView) findViewById(R.id.rv_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

}

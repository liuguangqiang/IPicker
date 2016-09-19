package com.liuguangqiang.ipicker.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.liuguangqiang.ipicker.IPicker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SelectedAdapter adapter;
    private ArrayList<String> selectPictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
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

        IPicker.setListener(new IPicker.OnSelectListener() {
            @Override
            public void onSelected(List<String> paths) {
                selectPictures.clear();
                selectPictures.addAll(paths);
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new SelectedAdapter(getApplicationContext(), selectPictures);
        recyclerView = (RecyclerView) findViewById(R.id.rv_photos);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

}

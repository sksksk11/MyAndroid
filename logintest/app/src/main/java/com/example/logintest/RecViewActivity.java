package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class RecViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_view);

        initData();

        mRecyclerView = findViewById(R.id.rv_scrolltest);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyRecyAdapter(this,mDatas));
    }

    void initData(){
        mDatas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(""+i);
        }
    }


}
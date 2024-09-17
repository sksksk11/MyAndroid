package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.mymusic.adapter.MyHistorylistAdapter;
import com.example.mymusic.data.WebHistory;
import com.example.mymusic.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Context mContext;

    private List<WebHistory> mWebHistoryList ;

    private MyHistorylistAdapter mHistorylistAdapter;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initView();

        initDatas();

        initClickEvent();  //设置点击事件

    }

    private void initClickEvent() {

        //调用接口获取点击的数据
        mHistorylistAdapter.setOnClickListener(new MyHistorylistAdapter.onClickListener() {
            @Override
            public void onItemClickListener(int position) {
                WebHistory webHistory = mWebHistoryList.get(position);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result",webHistory);
                setResult(RESULT_OK,resultIntent);
                finish();

            }
        });
    }

    private void initDatas() {
        mWebHistoryList = new ArrayList<>();
//        WebHistory mhistory1 = new WebHistory(1,"1111","aaaaa",null,null);
//        WebHistory mhistory2 = new WebHistory(2,"2222","bbbbb",null,null);
//        WebHistory mhistory3 = new WebHistory(3,"3333","ccccc",null,null);
//        mWebHistoryList.add(mhistory1);
//        mWebHistoryList.add(mhistory2);
//        mWebHistoryList.add(mhistory3);
        mDatabaseHelper = new DatabaseHelper(this);
        mWebHistoryList = mDatabaseHelper.getAllWebHistoryList();
        //处理获取列表为空时的情况

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mHistorylistAdapter = new MyHistorylistAdapter(this,mWebHistoryList);

        mRecyclerView.setAdapter(mHistorylistAdapter);



    }

    private void initView() {

        mRecyclerView = findViewById(R.id.rv_historyRecords);
        mContext = this;

    }
}
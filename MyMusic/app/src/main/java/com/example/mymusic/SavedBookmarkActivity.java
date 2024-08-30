package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.mymusic.adapter.MyBookmarkListAdapter;
import com.example.mymusic.data.WebInfo;
import com.example.mymusic.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class SavedBookmarkActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView ;
    private List<WebInfo> mWebInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_bookmark);

        //初始化页面
        initView();
        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //初始化网页数据
        initDatas();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        MyBookmarkListAdapter myBookmarkListAdapter = new MyBookmarkListAdapter(mWebInfoList,this);
        mRecyclerView.setAdapter(myBookmarkListAdapter);

    }

    private void initDatas() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllBookmarks();

        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String title  = cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_WEBTITLE));
                @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_WEBURL));
                @SuppressLint("Range") int icon = cursor.getInt(cursor.getColumnIndex(databaseHelper.COLUMN_ICON));
                Log.d("TAG", "title: "+title +" ,url:"+url+" ,icon:"+icon);

                WebInfo webInfo = new WebInfo(url,title,icon);
                mWebInfoList.add(webInfo);

            }while (cursor.moveToNext());

            cursor.close();
        }

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rv_savedBookmark);

    }
}
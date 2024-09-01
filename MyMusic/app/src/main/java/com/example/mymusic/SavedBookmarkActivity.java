package com.example.mymusic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusic.adapter.MyBookmarkListAdapter;
import com.example.mymusic.data.WebInfo;
import com.example.mymusic.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class SavedBookmarkActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView ;
    private List<WebInfo> mWebInfoList = new ArrayList<>();
    private TextView tv_title;
    private MyBookmarkListAdapter.onItemClickListener mItemClickListener;

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

        //调用接口获取点击的数据
        myBookmarkListAdapter.setItemClickListener(new MyBookmarkListAdapter.onItemClickListener() {
            @Override
            public void onItemSelected(int position) {
                Log.d("TAG", "通过接口调用: "+position);
                Intent resultIntent = new Intent();
                //传递对象（前提，WebInfo 通过Serializable 实现序列化）
                resultIntent.putExtra("result",mWebInfoList.get(position));
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

        //调用长按接口
        myBookmarkListAdapter.setLongClickListener(new MyBookmarkListAdapter.onLongClickListener() {
            @Override
            public void onItemLongSelected(int position) {
                Log.d("TAG", "长按接口调用: "+position);
                showEditDialog(position);
            }
        });




    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑书签");

        EditText editText = new EditText(this);
        editText.setText(mWebInfoList.get(position).getWebTitle());
        builder.setView(editText);

        builder.setNegativeButton("取消",null);

        builder.setNeutralButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //选择删除按钮
            }
        });

        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTitle = editText.getText().toString().trim();
                int webInfoId = mWebInfoList.get(position).getId();
                Log.d("TAG", "newTitle: "+newTitle+",ID:"+webInfoId);
                //按ID更新数据库

                Log.d("TAG", "修改后保存: ");
                recreate(); //重新加载页面
            }
        });



        builder.show();

    }

    private void initDatas() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getAllBookmarks();

        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String title  = cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_WEBTITLE));
                @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_WEBURL));
                @SuppressLint("Range") int icon = cursor.getInt(cursor.getColumnIndex(databaseHelper.COLUMN_ICON));
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(databaseHelper.COLUMN_ID));
                Log.d("TAG", "id: "+id +"title: "+title +" ,url:"+url+" ,icon:"+icon);

                WebInfo webInfo = new WebInfo(url,title,icon,id);
                mWebInfoList.add(webInfo);

            }while (cursor.moveToNext());

            cursor.close();
        }

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rv_savedBookmark);


    }
}
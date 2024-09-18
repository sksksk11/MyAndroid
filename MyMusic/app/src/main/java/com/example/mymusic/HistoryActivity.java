package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    private ImageView iv_searchImage , iv_clearSearchText;
    private EditText et_searchBar;

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

        //点击搜索按钮
        iv_searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = et_searchBar.getText().toString().trim();
                if (searchString != null && searchString.length()>=2) {
                    List<WebHistory> webHistoryList = mDatabaseHelper.getHistoryByKeyword(searchString);
//                    Log.d("TAG", "webHistoryList: "+webHistoryList.toString());


                }else {
                    Toast.makeText(mContext,"搜索字符长度小于2",Toast.LENGTH_SHORT).show();
                }


            }
        });

        //搜索框输入变化时,执行搜索
        et_searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TAG", "et_searchBar:有输入变化 ");
                String searchString = et_searchBar.getText().toString().trim();
                if (searchString != null) {
                    List<WebHistory> webHistoryList = mDatabaseHelper.getHistoryByKeyword(searchString);
//                    Log.d("TAG", "webHistoryList: "+webHistoryList.toString());
                    if (webHistoryList != null ) {

                        mWebHistoryList.clear();
                        mWebHistoryList.addAll(webHistoryList);
                        mHistorylistAdapter.notifyDataSetChanged();
                    }else if(webHistoryList.size() == 0){
                        Toast.makeText(mContext,"未搜索到:"+searchString,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        iv_clearSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_searchBar.setText("");
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
        iv_searchImage = findViewById(R.id.iv_searchImage);
        et_searchBar = findViewById(R.id.et_searchBar);
        iv_clearSearchText = findViewById(R.id.iv_clearSearchText);

        mContext = this;

    }
}
package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mymusic.adapter.MyKeywordListAdapter;
import com.example.mymusic.data.Keyword;
import com.example.mymusic.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class KeywordActivity extends AppCompatActivity {

    private Button btn_addKeyWord,btn_confirmInput;
    private EditText et_inputKeyword;
    private DatabaseHelper mDatabaseHelper;
    private List<Keyword> mKeywordList;
    private MyKeywordListAdapter mKeywordListAdapter;
    private RecyclerView mRecyclerView;
    private TextView tv_keyword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initView();

        initDatas();

        initeInteraction(); //初始化交互事件

    }

    private void initeInteraction() {
        //设置各控件点击事件


        //新增关键词按钮
        btn_addKeyWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_inputKeyword.setVisibility(View.VISIBLE);
                et_inputKeyword.requestFocus();
                btn_confirmInput.setVisibility(View.VISIBLE);

            }
        });

        //输入确认按钮
        btn_confirmInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入数据
                String inputStr = et_inputKeyword.getText().toString().trim();
                getInputStringToDataBase(inputStr);

                et_inputKeyword.setVisibility(View.GONE);
                btn_confirmInput.setVisibility(View.GONE);
            }
        });

        //点击关键词，复制到粘贴板，点击次数+1
        mKeywordListAdapter.setOnClickKeywordListener(new MyKeywordListAdapter.OnClickKeywordListener() {
            @Override
            public void OnItemclick(int position) {
                Keyword keyword = mKeywordList.get(position);
                Log.d("TAG", "点击keyword: "+keyword.toString());

                //复制到粘贴板

                //按id，点击次数+1 更新到数据库
                mDatabaseHelper.updateClickTimesById(keyword.getId(),keyword.getClicktimes());

                recreate();
            }
        });


    }

    private void initDatas() {
        mDatabaseHelper = new DatabaseHelper(this);
        mKeywordList = new ArrayList<>();
        mKeywordList = mDatabaseHelper.getAllKeywordList(1,true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mKeywordListAdapter = new MyKeywordListAdapter(this,mKeywordList);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mKeywordListAdapter);

    }

    private void initView() {
        btn_addKeyWord = findViewById(R.id.btn_addKeyWord);
        et_inputKeyword = findViewById(R.id.et_inputKeyword);
        btn_confirmInput = findViewById(R.id.btn_confirmInput);
        mRecyclerView = findViewById(R.id.rv_keywords);
        tv_keyword = findViewById(R.id.tv_keyword);

    }



    //将输入的多个关键词，批量导入数据库
    private int getInputStringToDataBase( String inputStr ) {
        int rows = 0;

        if (inputStr != null && !inputStr.isEmpty()) {

            //按换行符拆分未单个关键词
            String[] lines = inputStr.split("\n");
            for(String line : lines){
                if (line != null && !line.trim().isEmpty()){

                    Log.d("TAG", "line: "+line);
                    Keyword keyword = new Keyword(line,0,null);
                    //插入数据库
                    int insertRows = mDatabaseHelper.insertKeywordIntoDataBase(keyword);
                    if(insertRows == 1) { rows++; }
                }
            }
        }

        return rows;
    }
}
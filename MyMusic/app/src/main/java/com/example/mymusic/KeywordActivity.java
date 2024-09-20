package com.example.mymusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusic.adapter.MyKeywordListAdapter;
import com.example.mymusic.data.Keyword;
import com.example.mymusic.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class KeywordActivity extends AppCompatActivity {

    private Button btn_addKeyWord,btn_confirmInput,btn_clearText,btn_pasteboard;
    private EditText et_inputKeyword;
    private DatabaseHelper mDatabaseHelper;
    private List<Keyword> mKeywordList;
    private MyKeywordListAdapter mKeywordListAdapter;
    private RecyclerView mRecyclerView;
    private TextView tv_keyword;
    private Context mContext;
    private LinearLayout ll_btnContainer;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_keyword,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_multdelete:
                Log.d("TAG", "点击批量删除");
                return true;
            case R.id.menu_modify:
                Log.d("TAG", "点击修改");
                return true;
            case R.id.menu_orderByClicktimes:
                Log.d("TAG", "按点击次数排序");
                return true;
            case R.id.menu_orderByName:
                Log.d("TAG", "按点名称排序");
                return true;
            case R.id.menu_orderByAddition:
                Log.d("TAG", "按点添加顺序排序");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
                ll_btnContainer.setVisibility(View.VISIBLE);

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
                ll_btnContainer.setVisibility(View.GONE);
            }
        });

        //点击关键词，复制到粘贴板，点击次数+1
        mKeywordListAdapter.setOnClickKeywordListener(new MyKeywordListAdapter.OnClickKeywordListener() {
            @Override
            public void OnItemclick(int position) {
                Keyword keyword = mKeywordList.get(position);
                Log.d("TAG", "点击keyword: "+keyword.toString());

                //复制到粘贴板
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("keyword", keyword.getKeyword());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(mContext,"已复制："+keyword.getKeyword(),Toast.LENGTH_SHORT).show();

                //按id，点击次数+1 更新到数据库
                mDatabaseHelper.updateClickTimesById(keyword.getId(),keyword.getClicktimes());

                finish();
            }
        });

        //清空按钮
        btn_clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_inputKeyword.setText("");
            }
        });

        //读取剪贴板按钮,粘贴到文本框
        btn_pasteboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard.hasPrimaryClip()) {
                    ClipData clipData = clipboard.getPrimaryClip();
                    if (clipData.getItemCount()>0) {
                        ClipData.Item item = clipData.getItemAt(0);
                        CharSequence pasteText = item.getText().toString().trim();
                        Log.d("TAG", "pasteText: "+pasteText);
                        if (pasteText != null&& !((String) pasteText).isEmpty()) {
                            et_inputKeyword.setText(pasteText);
                            et_inputKeyword.setSelection(et_inputKeyword.getText().length());  //焦点放置在文本最后一位
                          }
                    }
                }
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

        ll_btnContainer = findViewById(R.id.ll_btnContainer);
        btn_clearText = findViewById(R.id.btn_clearText);
        btn_pasteboard = findViewById(R.id.btn_pasteboard);
        et_inputKeyword = findViewById(R.id.et_inputKeyword);

        mContext = this;




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
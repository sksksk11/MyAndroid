package com.example.mymusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
    private EditText et_inputKeyword ,et_searchBar;
    private DatabaseHelper mDatabaseHelper;
    private List<Keyword> mKeywordList,selectKeywordList;
    private MyKeywordListAdapter mKeywordListAdapter;
    private RecyclerView mRecyclerView;
//    private TextView tv_keyword;
    private Context mContext;
    private LinearLayout ll_btnContainer;
    private CheckBox cb_checkBox;
    private ImageView iv_searchImage,iv_clearSearchText;

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

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_keyword,menu);

        return true;
    }

    //选择菜单项
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_multselect:
                Log.d("TAG", "点击多选");
                selectKeywordList.clear();   //清空已选择的关键词
                return true;
            case R.id.menu_multdelete:
                Log.d("TAG", "点击批量删除");
                if (selectKeywordList.size() == 0) {
                    Toast.makeText(mContext,"请选择关键词",Toast.LENGTH_SHORT).show();
                    return true;
                }
                //获取已选择的关键词
                String selectedStr = "";

                for(Keyword w :selectKeywordList){
                    selectedStr = selectedStr + w.getKeyword()+" | " ;
                }
                TextView selectedStrTextView = new TextView(mContext);
                selectedStrTextView.setText(selectedStr);
                selectedStrTextView.setPadding(10,5,10,5); ;

                //弹出对话框确认是否删除
                new AlertDialog.Builder(mContext)
                        .setMessage("是否删除以下关键词：")
                        .setView(selectedStrTextView)
                        .setPositiveButton("确定",((dialog, which) -> {
                            for(Keyword w :selectKeywordList){
                                mDatabaseHelper.deleteKeywordById(w.getId());
                            }
                            recreate(); //刷新页面
                        }))
                        .setNegativeButton("取消",(dialog, which) -> {

                        })
                        .show();


                return true;
            case R.id.menu_modify:
                Log.d("TAG", "点击修改");

                //选择多行时，提醒只能一次修改一个关键词
                if (selectKeywordList.size() > 1 ) {
                    Toast.makeText(mContext,"一次只能修改一个关键词",Toast.LENGTH_SHORT).show();

                }else if(selectKeywordList.size() == 0){
                    Toast.makeText(mContext,"请选择一个要修改的关键词",Toast.LENGTH_SHORT).show();
                }else if(selectKeywordList.size() ==1){
                    Keyword newKeyword = selectKeywordList.get(0);
                    EditText editStrTextView = new EditText(mContext);
                    editStrTextView.setText(newKeyword.getKeyword());

                    //弹出对话框
                    new AlertDialog.Builder(mContext)
                            .setMessage("是否修改关键词：")
                            .setView(editStrTextView)
                            .setPositiveButton("确定",((dialog, which) -> {
                                //更新数据
                                mDatabaseHelper.updateKeywordById(newKeyword.getId(),editStrTextView.getText().toString().trim());
                                recreate(); //刷新页面
                            }))
                            .setNegativeButton("取消",(dialog, which) -> {

                            })
                            .show();

                }



                return true;
            case R.id.menu_orderByClicktimes:
                Log.d("TAG", "按点击次数排序");

                List<Keyword> orderByClicktimesList = new ArrayList<>();
                orderByClicktimesList = mDatabaseHelper.getAllKeywordList(3,true);
                mKeywordList.clear();
                mKeywordList.addAll(orderByClicktimesList);
                mKeywordListAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_orderByName:
                Log.d("TAG", "按点名称排序");
                List<Keyword> orderByNameList = new ArrayList<>();
                orderByNameList = mDatabaseHelper.getAllKeywordList(2,false);
                mKeywordList.clear();
                mKeywordList.addAll(orderByNameList);
                mKeywordListAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_orderByAddition:
                Log.d("TAG", "按点添加顺序排序");

                List<Keyword> orderByIdList = new ArrayList<>();
                orderByIdList = mDatabaseHelper.getAllKeywordList(1,true);
                mKeywordList.clear();
                mKeywordList.addAll(orderByIdList);
                mKeywordListAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //屏幕选择时



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
                recreate();
            }
        });

        //点击关键词，复制到粘贴板，点击次数+1
        mKeywordListAdapter.setOnClickKeywordListener(new MyKeywordListAdapter.OnClickKeywordListener() {
            //选择item
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

            //选择checkbox
            @Override
            public void OnCheckboxChecked(int position, boolean isChecked) {

                Log.d("TAG", "checkbox: "+position + " isChecked："+isChecked);
                if (isChecked) {
                    selectKeywordList.add(mKeywordList.get(position));
                    Log.d("TAG", "selectKeywordList: "+selectKeywordList.toString());
                }else {
                    selectKeywordList.remove(mKeywordList.get(position));
                    Log.d("TAG", "selectKeywordList: "+selectKeywordList.toString());
                }

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

        //复选框选择

        //搜索按钮

        //搜索框文字输入监听
        et_searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Keyword> getKeywordList = new ArrayList<>();
                String inputStr = et_searchBar.getText().toString().trim();

                getKeywordList = mDatabaseHelper.getKeywordListByStr(inputStr);
                mKeywordList.clear();
                mKeywordList.addAll(getKeywordList);
                mKeywordListAdapter.notifyDataSetChanged();

            }
        });

        //清空查询框
        iv_clearSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_searchBar.setText("");
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
//        et_inputKeyword = findViewById(R.id.et_inputKeyword);
        btn_confirmInput = findViewById(R.id.btn_confirmInput);
        mRecyclerView = findViewById(R.id.rv_keywords);
//        tv_keyword = findViewById(R.id.tv_keyword);
        cb_checkBox = findViewById(R.id.cb_checkBox);

        ll_btnContainer = findViewById(R.id.ll_btnContainer);
        btn_clearText = findViewById(R.id.btn_clearText);
        btn_pasteboard = findViewById(R.id.btn_pasteboard);
        et_inputKeyword = findViewById(R.id.et_inputKeyword);

        iv_searchImage = findViewById(R.id.iv_searchImage);
        et_searchBar = findViewById(R.id.et_searchBar);
        iv_clearSearchText = findViewById(R.id.iv_clearSearchText);

        mContext = this;

        selectKeywordList = new ArrayList<>();

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
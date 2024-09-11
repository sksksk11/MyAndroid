package com.example.mymusic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusic.adapter.MyBookmarkListAdapter;
import com.example.mymusic.adapter.MyIconsAdapter;
import com.example.mymusic.data.WebInfo;
import com.example.mymusic.utils.DatabaseHelper;
import com.example.mymusic.utils.ListTools;
import com.example.mymusic.utils.MyItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedBookmarkActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView ;
    private List<WebInfo> mWebInfoList = new ArrayList<>();
    private List<WebInfo> initialList = new ArrayList<>();  //保存初始列表，用于判断是否已更新数据，退出页面时提醒保存
    private TextView tv_title;
    private MyBookmarkListAdapter.onItemClickListener mItemClickListener;
    private MyBookmarkListAdapter myBookmarkListAdapter;
    private Button btn_keyWord;
    private EditText et_searchBar;
    private ImageView iv_searchImage,iv_clearSearchText;

    private Context mContext;

    private MyIconsAdapter mMyIconsAdapter;
    private int[] iconIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_bookmark);

        //初始化页面
        initView();

        //按文本搜索书签
        searchMarkbook();

        //隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //初始化网页数据
        initDatas();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        myBookmarkListAdapter = new MyBookmarkListAdapter(mWebInfoList,this);

        MyItemTouchHelperCallback callback = new MyItemTouchHelperCallback(myBookmarkListAdapter,mRecyclerView,getBaseContext());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

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

//        调用长按接口,先禁用避免与拖拽冲突，改用侧滑按钮实现
        myBookmarkListAdapter.setLongClickListener(new MyBookmarkListAdapter.onLongClickListener() {
            @Override
            public void onItemLongSelected(int position) {
                Log.d("TAG", "长按接口调用: "+position);
//                showEditDialog(position);
            }
        });


        //调用删除按钮接口、配置按钮接口
        myBookmarkListAdapter.setOndeleteButtonClickListener(new MyBookmarkListAdapter.deleteButtonClicked() {
            @Override
            public void onItemDismiss(int position) {
                int webInfoId = mWebInfoList.get(position).getId();
                deleteBookmarkById(webInfoId);
                mWebInfoList.remove(position);
                //通过删除按钮删除
                Log.d("TAG", "通过删除按钮删除: "+position);
                recreate();
            }

            @Override
            public void onItemConfig(int position) {
                Log.d("TAG", "点击配置按钮: "+position);
                WebInfo webInfo = mWebInfoList.get(position);
                int webinfoId=  webInfo.getId();

                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.config_webinfo_layout,null);
                Spinner spn_category = dialogView.findViewById(R.id.spn_category);


                EditText et_title = dialogView.findViewById(R.id.et_title);
                et_title.setText(webInfo.getWebTitle());

                ImageView iv_icon = dialogView.findViewById(R.id.iv_icon);
                final int[] newIconid = {webInfo.getIcon()};
                iv_icon.setImageResource(newIconid[0]);

                //icon图片列表
                GridView gv_iconImgs = dialogView.findViewById(R.id.gv_iconImgs);
                iconIds = new int[]{
                        R.drawable.logo_asana,
                        R.drawable.logo_confluence,
                        R.drawable.logo_coub,
                        R.drawable.logo_creativemarket,
                        R.drawable.logo_dailymotion,
                        R.drawable.logo_digg,
                        R.drawable.logo_framer,
                        R.drawable.logo_github,
                        R.drawable.logo_iconjar,
                        R.drawable.logo_intercom,
                        R.drawable.logo_iodgo_iscord,
                        R.drawable.logo_kickstarter,
                        R.drawable.logo_quora,
                        R.drawable.logo_rss,
                        R.drawable.logo_snapchat,
                        R.drawable.logo_spotify,
                        R.drawable.logo_strava,
                        R.drawable.logo_treehouse,
                        R.drawable.logo_ubuntu,
                        R.drawable.logo_whatsapp,
                        R.drawable.bm_default
                };
                mMyIconsAdapter = new MyIconsAdapter(mContext,iconIds);
                gv_iconImgs.setAdapter(mMyIconsAdapter);


                //从数据库读取当前所有分类
                DatabaseHelper databaseHelper = new DatabaseHelper(mContext);

                //加入到列表
                List<String> items = new ArrayList<>();
                items.addAll(databaseHelper.getAllCategory());

                //把未分类 放到列表最后
                items.remove(databaseHelper.CATEGORY_DEFAULT);
                items.add(databaseHelper.CATEGORY_DEFAULT);

                //创建一个ArrayAdapter来适配数据
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, items);

                //设置下拉列表样式
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                //将Adapter设置给spinner
                spn_category.setAdapter(adapter);
//                spn_category.setSelection(0,true);
                //设置默认选择当前分类
                int selectPosition = items.indexOf(webInfo.getCategory());
                if (selectPosition != -1) {
                    spn_category.setSelection(selectPosition,true);
                }

                //获取spinner选择结果
                final String[] selectString = {""};
                spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectString[0] = parent.getItemAtPosition(position).toString();
                        Log.d("TAG", "selectPosition[0]: "+selectString[0]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.d("TAG", "spinner没有选择任何选项");
                    }
                });


                //获取icon列表选择结果
                gv_iconImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("TAG", "ICON点击: "+position);

                        //设置图片背景？ 还是替换 imageview 图片？
                        newIconid[0] = iconIds[position];
                        iv_icon.setImageResource(newIconid[0]);
                    }
                });


                new AlertDialog.Builder(mContext )
                        .setMessage("修改书签信息")
                        .setView(dialogView)
                        .setPositiveButton("保存",(dialog, which) -> {
                            //保存修改后的书签信息
                            String newTitle = et_title.getText().toString().trim();
                            String newCategory = selectString[0].toString().trim();
                            if (newCategory == null || newCategory == "") { newCategory = webInfo.getCategory(); }

                            //按id更新数据库
                            Log.d("TAG", "newTitle: "+newTitle+"，newCategory:"+newCategory);
                            int updateRows = updateBookmarkTitleById(webinfoId,newTitle,newCategory,newIconid[0]);

                            recreate();
                        })
                        .setNegativeButton("取消",(dialog, which) -> {

                        })
                        .show();

            }



            @Override
            public void getItemPosition(String oprCode, int position) {
                // oprCode = "toTop" ， 设置item 到列表第一位
                // oprCode = "toBottom" ，设置item 到列表最后一位
                List<WebInfo> webInfoList = myBookmarkListAdapter.getWebInfoList();

                //点击toTop按钮时
                if(oprCode == "toTop"){
                    Log.d("TAG", "已点击toTop按钮: ");
                    //把被点击item放到列表第一位
                    WebInfo webInfo = webInfoList.get(position);
                    ListTools.changeBookmarkOrder(webInfoList,webInfo,0);

                }else if(oprCode == "toBottom"){
                    Log.d("TAG", "已点击toBottom按钮: ");
                    //把被点击item放到列表最后一位
                    WebInfo webInfo = webInfoList.get(position);
                    ListTools.changeBookmarkOrder(webInfoList,webInfo,-1);

                }
                //写入数据库
//                Log.d("TAG", "排序后: "+webInfoList.toString());
                updateBookmarkListOrder(webInfoList);
                recreate();

            }
        });

    }


    private void searchMarkbook() {
        iv_searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "已点击搜索图片");
                String searchText = et_searchBar.getText().toString().trim();
                List<WebInfo> webInfoList = new ArrayList<>();
                List<WebInfo> resultList = new ArrayList<>();  //搜索结果
                webInfoList.addAll(myBookmarkListAdapter.getWebInfoList());

                if (searchText == null || searchText.length()==0 || webInfoList == null) {
                    recreate();
                    return;
                }else{
                    Log.d("TAG", "开始搜索: "+searchText);
                    //获取当前书签列表，查找title和url是否包含查找的文本
                    for (WebInfo w: webInfoList
                    ) {
                        if(w.getWebTitle().contains(searchText) || w.getWebUrl().contains(searchText)){
                            resultList.add(w);
                        }
                    }
                    //查找到结果
                    if (resultList.size() > 0 ) {
                        Log.d("TAG", "搜索到结果: "+resultList.toString());
                        myBookmarkListAdapter.getWebInfoList().clear();
                        myBookmarkListAdapter.getWebInfoList().addAll(resultList);
                        myBookmarkListAdapter.notifyDataSetChanged();
                        //同步更新初始列表，防止判断位更新列表，弹出保存对话框
                        initialList.clear();
                        initialList.addAll(resultList);

                    }else{
                        Log.d("TAG", "没搜到 ");
                        //提示没收到结果
                        Toast.makeText(mContext,"没搜到 :( ",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //当输入文本时，出现X按钮，点击x按钮，重新加载页面 （就自动清空已输入的查询文本）
        iv_clearSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "已点击清空按钮123123");
                et_searchBar.setText("");
                et_searchBar.clearFocus();
                recreate();
            }
        });

    }

//    private void showEditDialog(int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("编辑书签");
//
//        EditText editText = new EditText(this);
//        editText.setText(mWebInfoList.get(position).getWebTitle());
//        builder.setView(editText);
//
//        builder.setNegativeButton("取消",null);
//
//        builder.setNeutralButton("删除", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //选择删除按钮,按ID删除当前条目，更新所有书签排列序号
//
//                Log.d("TAG", "删除前列表: "+ Arrays.toString(mWebInfoList.toArray()) );//打印列表供测试
//
//                int webInfoId = mWebInfoList.get(position).getId();
//                int deletedRows = deleteBookmarkById(webInfoId);
//                Log.d("TAG", "deletedRows: "+deletedRows);
//                mWebInfoList.remove(position);
//                updateBookmarkListOrder(mWebInfoList);
//                Log.d("TAG", "删除后的列表: "+ Arrays.toString(mWebInfoList.toArray()) );//打印列表供测试
//                recreate(); //重新加载页面
//            }
//        });
//
//        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String newTitle = editText.getText().toString().trim();
//                int webInfoId = mWebInfoList.get(position).getId();
//                String newCategory = null;   //还未加控件
//                Log.d("TAG", "newTitle: "+newTitle+",ID:"+webInfoId+"，newCategory："+newCategory);
//                //按ID更新数据库中的title
//                int updatedRows = updateBookmarkTitleById(webInfoId,newTitle,newCategory);
//
//                Log.d("TAG", "修改后保存,更新数据条目："+updatedRows);
//                recreate(); //重新加载页面
//            }
//        });
//        builder.show();
//    }


    private int deleteBookmarkById(int webInfoId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        int deletedRows = databaseHelper.deleteBookmarkById(webInfoId);
        return deletedRows;
    }

    //传入新的列表，重新排序
    private int updateBookmarkListOrder(List<WebInfo> newWebInfoList){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        //更新数据库内数据排序
        int updateRows = databaseHelper.updateBookmarkListOrderNum(newWebInfoList);
        return updateRows;
    }

    private int updateBookmarkTitleById(int webInfoId, String newTitle,String newCategory ,int newIconid) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        int updatedRows = databaseHelper.updataTitleById(webInfoId,newTitle,newCategory,newIconid);
        return updatedRows;
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
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_CATEGORY));
//                @SuppressLint("Range") int Num = cursor.getInt(cursor.getColumnIndex(databaseHelper.COLUMN_NUMBER));
                Log.d("TAG", "id: "+id +" , title: "+title +" ,url:"+url+" ,icon:"+icon+" ,category:"+category);

                WebInfo webInfo = new WebInfo(url,title,icon,id,category);
                mWebInfoList.add(webInfo);
                //同时复制一份到初始列表里
                initialList.add(webInfo);

            }while (cursor.moveToNext());
            cursor.close();

        }

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rv_savedBookmark);
        btn_keyWord = findViewById(R.id.btn_keyWord);
        et_searchBar = findViewById(R.id.et_searchBar);
        iv_searchImage = findViewById(R.id.iv_searchImage);
        iv_clearSearchText = findViewById(R.id.iv_clearSearchText);

        mContext = this;

    }

    public void saveListSort(View view) {
        //点击按钮，保存当前列表顺序到数据库
        List<WebInfo> newWebInfoList = myBookmarkListAdapter.getWebInfoList();
        Log.d("TAG", "当前列表: "+newWebInfoList.toString());
        //保存到数据库
        int updateRows = updateBookmarkListOrder(newWebInfoList);
        recreate();
        Toast.makeText(this,"已更新列表顺序："+updateRows,Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {

        List<WebInfo> newWebInfoList = myBookmarkListAdapter.getWebInfoList();
        if(!ListTools.compareListOrder(initialList,newWebInfoList)) {
            new AlertDialog.Builder(this)
                    .setMessage("书签顺序已改变，是否保存？")
                    .setPositiveButton("保存", (dialog, which) -> {
                        // 用户选择保存数据
                        // 可能需要执行的其他操作，例如关闭活动
                        saveListSort(null);
                        finish();
                    })
                    .setNegativeButton("取消", (dialog, which) -> {
                        // 用户选择放弃数据
                        // 通常不需要执行任何操作，因为活动会保持在当前状态

                    })
                    .setNeutralButton("退出", (dialog, which) -> {
                        // 用户选择取消操作，不退出活动
                        // 可能需要执行的其他操作，例如关闭活动

                        finish();
                    })
                    .show();

        }else{
            super.onBackPressed();
        }


    }

    public void showKeyWord(View view) {
        //点击关键词按钮

        //测试，待删，将列表第3位移动到第一位
        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
        stringList.add("4");

        Log.d("TAG", "变更前: "+stringList.toString());
        stringList.add(3,stringList.remove(2));
        Log.d("TAG", "变更后: "+stringList.toString());

    }
}
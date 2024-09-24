package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mymusic.adapter.MyImageAdapter;
import com.example.mymusic.data.WebImage;
import com.example.mymusic.utils.DatabaseHelper;
import com.example.mymusic.utils.timeTools;

import java.util.ArrayList;
import java.util.List;

public class WebImagesActivity extends AppCompatActivity {

    private RecyclerView rv_imgsContainer;
    private Button btn_collectImgs;
    private MyImageAdapter mImageAdapter;
    private DatabaseHelper mDatabaseHelper;
    private List<WebImage> mWebImageList;
    private Context mContext;
    private ImageView iv_bigImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_images);

        initView();

        initDatas();

        initeInteraction(); //初始化交互事件
    }

    private void initeInteraction() {
        //点击RecylerView中的图片时，放大显示
        mImageAdapter.setOnItemClickedListener(new MyImageAdapter.OnItemClickedListener() {
            @Override
            public void OnItemClicked(int position) {
                WebImage img = mWebImageList.get(position);
                Log.d("TAG", "图片: "+img.getImgUrl());

                //使用Glide 库加载网络图片
                Glide.with(mContext)
                        .load(img.getImgUrl())
                        .into(iv_bigImage);

                iv_bigImage.setVisibility(View.VISIBLE);    //显示大图
                rv_imgsContainer.setVisibility(View.GONE);   //隐藏图片列表
            }

        });
    }

    private void initDatas() {
        //模拟数据测试用，待删
//        String imgUrl1 = "https://nimg.ws.126.net/?url=http%3A%2F%2Fcms-bucket.ws.126.net%2F2024%2F0923%2F5c088180p00sk91a4000zc0009c0070c.png&thumbnail=330x2147483647&quality=75&type=webp";
//        String time1 = timeTools.getCurrentTimeFormatted();
//        WebImage img1 = new WebImage(imgUrl1,time1,true);
//        mWebImageList.add(img1);
//
//        String imgUrl2 = "https://developer.android.google.cn/static/images/training/basics/intent-chooser.png?hl=zh-cn";
//        String time2 = timeTools.getCurrentTimeFormatted();
//        WebImage img2 = new WebImage(imgUrl2,time2,true);
//        mWebImageList.add(img2);
//
//        String imgUrl3 = "https://pics6.baidu.com/feed/c8ea15ce36d3d539d0bbd79569ab5d5d342ab098.jpeg@f_auto?token=23a742d62cb7809fda0b99cdcaeb0f8c";
//        String time3 = timeTools.getCurrentTimeFormatted();
//        WebImage img3 = new WebImage(imgUrl3,time3,true);
//        mWebImageList.add(img3);
//
//        String imgUrl4 = "https://t11.baidu.com/it/u=610394280,245389405&fm=30&app=106&f=JPEG?w=312&h=208&s=C3107C8D309101DA1ABC65270300F06A";
//        String time4 = timeTools.getCurrentTimeFormatted();
//        WebImage img4 = new WebImage(imgUrl4,time4,true);
//        mWebImageList.add(img4);

        //接收传过来的图片地址列表
        List<String> webImageList = getIntent().getStringArrayListExtra("WebImageList");
//        Log.d("TAG", "WebImageList: "+webImageList.toString());
        if(webImageList!=null && webImageList.size()>0){
            for(String imgUrl : webImageList){
                WebImage img = new WebImage(imgUrl,timeTools.getCurrentTimeFormatted(),true);
                mWebImageList.add(img);
            }

        }


        //设置RecylerView
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv_imgsContainer.setLayoutManager(layoutManager);

        mImageAdapter = new MyImageAdapter(mContext,mWebImageList);
        rv_imgsContainer.setAdapter(mImageAdapter);



    }

    private void initView() {
        rv_imgsContainer = findViewById(R.id.rv_imgsContainer);
        btn_collectImgs = findViewById(R.id.btn_collectImgs);
        mWebImageList = new ArrayList<>();

        iv_bigImage = findViewById(R.id.iv_bigImage);

        mContext = this;

    }

    //点击返回键时
    @Override
    public void onBackPressed() {
        //如果大图的imageview时显示的，则隐藏，否则直接退出
        if(iv_bigImage.getVisibility() == View.VISIBLE){
            iv_bigImage.setVisibility(View.GONE);
            rv_imgsContainer.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }


    }
}
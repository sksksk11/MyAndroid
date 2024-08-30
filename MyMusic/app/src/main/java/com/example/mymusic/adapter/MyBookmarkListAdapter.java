package com.example.mymusic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.data.WebInfo;

import java.util.List;

public class MyBookmarkListAdapter extends RecyclerView.Adapter<MyBookmarkListAdapter.MybookmarkViewHolder> {

    private List<WebInfo> mWebInfoList;
    private Context mContext;

    private onItemClickListener mItemClickListener;

    public MyBookmarkListAdapter(List<WebInfo> webInfoList, Context context) {
        mWebInfoList = webInfoList;
        mContext = context;
    }

    class MybookmarkViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_icon;
        private TextView et_pageTitle, et_pageUrl;
        private LinearLayout ll_container;

        public MybookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            et_pageTitle = itemView.findViewById(R.id.et_Pagetitle);
            et_pageUrl = itemView.findViewById(R.id.et_pageUrl);
            ll_container = itemView.findViewById(R.id.ll_container);


//            ll_container.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    if(mItemClickListener !=null) {
////                        mItemClickListener.onItemClick(getAdapterPosition());
////                    }
//
//
//
//                }
//            });

        }

        private void bind(WebInfo webInfo) {
//            iv_icon.setImageResource(R.id.);
            et_pageTitle.setText(webInfo.getWebTitle());
            et_pageUrl.setText(webInfo.getWebUrl());
        }
    }



    @NonNull
    @Override
    public MybookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bookmark,parent,false);
        MybookmarkViewHolder mybookmarkViewHolder = new MybookmarkViewHolder(view);
        //点击网页信息时的事件
        mybookmarkViewHolder.ll_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "书签被点击");  //测试用

                int position = mybookmarkViewHolder.getAdapterPosition();
                WebInfo webInfo = mWebInfoList.get(position);
                Log.d("TAG", "被点击: "+position);
                Toast.makeText(parent.getContext(),"已点击："+webInfo.getWebTitle().toString(),Toast.LENGTH_SHORT).show();

            }
        });

//        点击图标时的事件
        mybookmarkViewHolder.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "图片被点击");
            }
        });


        return mybookmarkViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MybookmarkViewHolder holder, int position) {
        WebInfo webInfo = mWebInfoList.get(position);
        holder.bind(webInfo);

    }

    @Override
    public int getItemCount() {
        return mWebInfoList == null ? 0:mWebInfoList.size();
    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(onItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

}

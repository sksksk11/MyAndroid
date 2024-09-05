package com.example.mymusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private onLongClickListener mOnLongClickListener;

    private deleteButtonClicked mDeleteButtonClicked;

    public MyBookmarkListAdapter(List<WebInfo> webInfoList, Context context) {
        mWebInfoList = webInfoList;
        mContext = context;

//        mDeleteItemButton = new deleteItemButton() {
//            @Override
//            public void onItemDismiss(int position) {
//                Log.d("TAG", "通过接口删除: "+position);
//                mWebInfoList.remove(position);
//                notifyItemRemoved(position);
//            }
//        };
    }

    class MybookmarkViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_icon;
        private TextView et_pageTitle, et_pageUrl;
        public LinearLayout ll_container;
        private Button btn_delete;
        private ImageView iv_delete,iv_config;

        public MybookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            et_pageTitle = itemView.findViewById(R.id.et_Pagetitle);
            et_pageUrl = itemView.findViewById(R.id.et_pageUrl);
            ll_container = itemView.findViewById(R.id.ll_container);
            btn_delete = itemView.findViewById(R.id.btn_delete);

            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_config = itemView.findViewById(R.id.iv_config);

        }

        private void bind(WebInfo webInfo) {
//            iv_icon.setImageResource(R.id.);
            et_pageTitle.setText(webInfo.getWebTitle());
            et_pageUrl.setText(webInfo.getWebUrl());
            //删除按钮,替换为图片删除按钮
//            btn_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("TAG", "delete: "+getAdapterPosition());
//                    mDeleteButtonClicked.onItemDismiss(getAdapterPosition());
//                }
//            });

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "iv_delete: "+getAdapterPosition());
                    mDeleteButtonClicked.onItemDismiss(getAdapterPosition());
                }
            });

            iv_config.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //通过接口传出被点击的位置
                    Log.d("TAG", "iv_delete: "+getAdapterPosition());
                    mDeleteButtonClicked.onItemConfig(getAdapterPosition());

                }
            });


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
                mItemClickListener.onItemSelected(position);

            }
        });

//        点击图标时的事件
        mybookmarkViewHolder.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "图片被点击");

            }
        });

        //长按时，弹出页面修改已保存页面标题，或删除
        mybookmarkViewHolder.ll_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("TAG", "长时间点击");  //测试用

                int position = mybookmarkViewHolder.getAdapterPosition();
                WebInfo webInfo = mWebInfoList.get(position);
                Log.d("TAG", "长时间点击: "+position);

                mOnLongClickListener.onItemLongSelected(position);

                return false;
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



    //点击选择item的接口
    public interface onItemClickListener{
        void onItemSelected(int position);
    }

    public void setItemClickListener(onItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }


    //长按选择item的接口
    public interface onLongClickListener{
        void onItemLongSelected(int position);
    }

    public void setLongClickListener(onLongClickListener itemLongClickListener){
        mOnLongClickListener = itemLongClickListener;
    }

    public List<WebInfo> getWebInfoList(){

        return mWebInfoList;
    }

    //删除按钮item接口
    public interface deleteButtonClicked{
        void onItemDismiss(int position);   //删除item用

        void onItemConfig(int position);    //配置item用

    }

    public void setOndeleteButtonClickListener(deleteButtonClicked deleteButtonClicked){
        this.mDeleteButtonClicked = deleteButtonClicked;
    }

}

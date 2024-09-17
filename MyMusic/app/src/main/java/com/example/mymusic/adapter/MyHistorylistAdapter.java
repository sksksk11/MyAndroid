package com.example.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.data.WebHistory;

import java.util.List;

public class MyHistorylistAdapter extends RecyclerView.Adapter<MyHistorylistAdapter.MyHistoryViewHolder> {

    private Context mContext;
    private List<WebHistory> mWebHistoryList;

    private onClickListener mOnClickListener;

    public MyHistorylistAdapter(Context context, List<WebHistory> webHistoryList) {
        mContext = context;
        mWebHistoryList = webHistoryList;
    }

    @NonNull
    @Override
    public MyHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.historylayout,parent,false);
        MyHistoryViewHolder myHistoryViewHolder = new MyHistoryViewHolder(view);

        myHistoryViewHolder.ll_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onItemClickListener(myHistoryViewHolder.getAdapterPosition());
            }
        });

        return myHistoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHistoryViewHolder holder, int position) {
        WebHistory webHistory = mWebHistoryList.get(position);

        holder.tv_webTitle.setText(webHistory.getWebTitle());
        holder.tv_webUrl.setText(webHistory.getWebUrl());

    }

    @Override
    public int getItemCount() {
        if (mWebHistoryList == null) {
            return 0;
        }else{ return mWebHistoryList.size(); }

    }

    class MyHistoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_webTitle ,tv_webUrl;
        private LinearLayout ll_container;

        public MyHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_webTitle = itemView.findViewById(R.id.tv_webTitle);
            tv_webUrl = itemView.findViewById(R.id.tv_webUrl);
            ll_container = itemView.findViewById(R.id.ll_container);

        }


    }

    //设置接口，获取点击位置
    public interface onClickListener{
        public void onItemClickListener(int position);

    }

    public void setOnClickListener(onClickListener onClickListener){
        mOnClickListener = onClickListener;
    }


}


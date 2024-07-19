package com.example.logintest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyAdapter extends RecyclerView.Adapter<MyRecyAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mDatas;

    public MyRecyAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holer = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item,parent,false));
        return holer;
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tv.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
//        return 0;
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_num);
        }
    }
}

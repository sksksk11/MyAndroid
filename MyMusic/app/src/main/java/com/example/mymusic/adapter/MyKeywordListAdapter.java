package com.example.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.data.Keyword;

import java.util.List;

public class MyKeywordListAdapter extends RecyclerView.Adapter<MyKeywordListAdapter.MyKeywordViewholder> {

    private Context mContext;
    private List<Keyword> mKeywordList;
    private OnClickKeywordListener myOnClickKeywordListener;

    public MyKeywordListAdapter(Context context, List<Keyword> keywordList) {
        mContext = context;
        mKeywordList = keywordList;
    }

    @NonNull
    @Override
    public MyKeywordViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.kewordlayout,parent,false);
        MyKeywordViewholder myKeywordViewholder = new MyKeywordViewholder(view);



        return myKeywordViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyKeywordViewholder holder, int position) {
        Keyword keyword = mKeywordList.get(position);

        holder.tv_keyword.setText(keyword.getKeyword());
        holder.tv_clicktimes.setText(keyword.getClicktimes()+"");

    }


    @Override
    public int getItemCount() {
        if (mKeywordList == null) {
            return 0;
        }else{ return mKeywordList.size(); }

    }



    public class MyKeywordViewholder extends RecyclerView.ViewHolder {

        private Button btn_addKeyWord;
        private CheckBox cb_checkBox;
        private TextView tv_keyword,tv_clicktimes;

        public MyKeywordViewholder(@NonNull View itemView) {
            super(itemView);

            btn_addKeyWord = itemView.findViewById(R.id.btn_addKeyWord);
            cb_checkBox = itemView.findViewById(R.id.cb_checkBox);
            tv_keyword = itemView.findViewById(R.id.tv_keyword);
            tv_clicktimes = itemView.findViewById(R.id.tv_clicktimes);

            tv_keyword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOnClickKeywordListener.OnItemclick(getAdapterPosition());
                }
            });


        }
    }

    public interface OnClickKeywordListener {

        public void OnItemclick(int position);

    }


    public void setOnClickKeywordListener(OnClickKeywordListener myOnClickKeywordListener){
        this.myOnClickKeywordListener = myOnClickKeywordListener;
    }


}

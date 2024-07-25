package com.example.mymusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.data.Song;

import java.util.ArrayList;

public class MySongListAdapter extends RecyclerView.Adapter<MySongListAdapter.MySongItemViewHolder> {

    private ArrayList<Song> mSongArrayList;
    private Context context;

    private MySongListAdapter(ArrayList<Song> songArrayList, Context context) {
        mSongArrayList = songArrayList;
        this.context = context;
    }

    class MySongItemViewHolder extends RecyclerView.ViewHolder{

        public MySongItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public MySongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MySongItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}

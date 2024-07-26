package com.example.mymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.data.Song;

import java.util.ArrayList;

public class MySongListAdapter extends RecyclerView.Adapter<MySongListAdapter.MySongItemViewHolder> {

    private ArrayList<Song> mSongArrayList;
    private Context context;

    public MySongListAdapter(ArrayList<Song> songArrayList, Context context) {
        mSongArrayList = songArrayList;
        this.context = context;
    }

    class MySongItemViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;

        public MySongItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_song_name);
        }

        public void bind(Song song){
            mTextView.setText(song.getSongName());
        }
    }

    @NonNull
    @Override
    public MySongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item_layout, parent, false);
        return new MySongItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySongItemViewHolder holder, int position) {
        Song song = mSongArrayList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mSongArrayList == null ? 0:mSongArrayList.size();
    }

}

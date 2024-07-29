package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mymusic.data.GlobalConstans;
import com.example.mymusic.data.Song;

import java.io.Serializable;
import java.util.ArrayList;

public class MusicPlayActivity extends AppCompatActivity {

    private ArrayList<Song> mSongArrayList;
    private int curSongIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        Intent intent = getIntent();
        curSongIndex = intent.getIntExtra(GlobalConstans.KEY_SONG_INDEX,0);
//        mSongArrayList = (ArrayList<Song>) intent.getSerializableExtra(GlobalConstans.KEY_SONG_LIST);
        mSongArrayList = intent.getParcelableArrayListExtra(GlobalConstans.KEY_SONG_LIST);
        Log.d("tag", "当前歌曲: "+curSongIndex);
        if(mSongArrayList!=null){
            Log.d("tag", "歌曲信息: "+mSongArrayList);
        }

    }




}
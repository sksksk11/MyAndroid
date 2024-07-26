package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mymusic.adapter.MySongListAdapter;
import com.example.mymusic.data.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btn_test ;
    private RecyclerView mRCVSongList;
    private MySongListAdapter mMySongListAdapter;
    private ArrayList<Song> mSongArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,MusicPlayActivity.class);
                startActivity(intent);
            }
        });

        initData();
        initSongList();

    }

    private void initData() {
        mSongArrayList = new ArrayList<>();
        mSongArrayList.add(new Song("song1.mp3"));
        mSongArrayList.add(new Song("song2.mp3"));
        mSongArrayList.add(new Song("song3.mp3"));
        mSongArrayList.add(new Song("song4.mp3"));
        mSongArrayList.add(new Song("song5.mp3"));

    }

    private void initSongList() {
        mMySongListAdapter = new MySongListAdapter(mSongArrayList,this);
        mRCVSongList.setAdapter(mMySongListAdapter); //加载数据
        mRCVSongList.setLayoutManager(new LinearLayoutManager(this));  //设置布局

    }

    private void initView() {
        btn_test = (Button) findViewById(R.id.btn_test);
        mRCVSongList = findViewById(R.id.rcv_song_lsit);

    }


}
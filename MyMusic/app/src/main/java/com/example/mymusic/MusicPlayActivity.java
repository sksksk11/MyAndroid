package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.mymusic.data.GlobalConstans;
import com.example.mymusic.data.Song;
import com.example.mymusic.service.MyMusicService;

import java.io.Serializable;
import java.util.ArrayList;

public class MusicPlayActivity extends AppCompatActivity {

    private ArrayList<Song> mSongArrayList;
    private int curSongIndex;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
           //服务已建立，传递数据（通过IBinder）
            MyMusicService.myMusicBinder musicBinder = (MyMusicService.myMusicBinder) iBinder;
            musicBinder.updateMusicList(mSongArrayList);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

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

        startMusicService();


    }

    private void startMusicService() {
        Intent intent = new Intent(this, MyMusicService.class);
        bindService(intent,conn,BIND_AUTO_CREATE) ;

    }


}
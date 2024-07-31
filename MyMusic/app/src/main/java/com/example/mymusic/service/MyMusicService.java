package com.example.mymusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.mymusic.data.Song;

import java.util.ArrayList;

public class MyMusicService extends Service {

    MediaPlayer mMediaPlayer;
    private ArrayList<Song> mSongArrayList;
    private int curSongIndex;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new myMusicBinder(this);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void updateMusicList(ArrayList<Song> songArrayList) {
        this.mSongArrayList = songArrayList;

    }


    
    ////////////////////////////////////////////////////////////

    public class myMusicBinder extends Binder {

        MyMusicService mMusicService;

        public myMusicBinder(MyMusicService myMusicService) {
            mMusicService = myMusicService;
        }

        public void startPlay(){

        }

        public void updateMusicList(ArrayList<Song> songArrayList){
            mMusicService.updateMusicList(songArrayList);

        }



    }



}

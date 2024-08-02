package com.example.mymusic.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mymusic.data.Song;
import com.example.mymusic.myimplements.musicImpl;
import com.example.mymusic.myimplements.musicImpl.onSongChangeListener;

import java.io.IOException;
import java.util.ArrayList;

public class MyMusicService extends Service {

    MediaPlayer mMediaPlayer;
    private ArrayList<Song> mSongArrayList;
    private int curSongIndex;
//    private Song curSong;  //当前播放歌曲
    private onSongChangeListener mSongChangeListener;

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

        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    private void updateMusicList(ArrayList<Song> songArrayList) {
        this.mSongArrayList = songArrayList;

    }

    private void updateCurrentMusicIndex(int index) {
        //防传入数据过大或过小, 严谨
        if(index<0 || index>= mSongArrayList.size() ){ return; }

        this.curSongIndex = index;
        //播放该条歌曲
        Song song = mSongArrayList.get(curSongIndex);
        String songName = song.getSongName();

        if(mSongChangeListener!=null) {
            mSongChangeListener.onSongChange(song);  //外传当前播放歌曲信息
        }

        AssetManager assetManager = getAssets();

        Log.d("tag", "正在播放歌曲: "+songName);

        try {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            AssetFileDescriptor fileDescriptor = assetManager.openFd(songName);
            mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean isPlaying() {
        return mMediaPlayer.isPlaying();

    }

    private void previous() {
        //播放上一首
        int index = curSongIndex -1;
        if(index <0 ){
            index = mSongArrayList.size()-1;
        }
//        Log.d("tag", "curSongIndex: "+index);
        updateCurrentMusicIndex(index);

    }

    private void next() {
        //播放下一首
        int index = curSongIndex + 1;
        if(index > mSongArrayList.size()-1 ){
            index = 0;
        }
//        Log.d("tag", "curSongIndex: "+index);
        updateCurrentMusicIndex(index);
    }

    private void stopMusic() {
        //停止播放音乐
        mMediaPlayer.stop();
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private int getCurTime(){
        //获取当前播放进度
        return mMediaPlayer.getCurrentPosition();
    }

    private int getDuration(){
        return mMediaPlayer.getDuration();
    }

    ////////////////////////////////////////////////////////////

    public class myMusicBinder extends Binder {

        MyMusicService mMusicService;

        public myMusicBinder(MyMusicService myMusicService) {
            mMusicService = myMusicService;
        }

        public void startPlay(){
            //如果已经在播放了，返回
            if(mMediaPlayer.isPlaying()){
                return;
            }
            mMediaPlayer.start();
        }

        public void pause() {
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }

        }

        public void updateMusicList(ArrayList<Song> songArrayList){
            mMusicService.updateMusicList(songArrayList);

        }

        public void updateCurrentMusicIndex(int index){
            mMusicService.updateCurrentMusicIndex(index);

        }


        public boolean isPlaying() {
           return mMusicService.isPlaying();
        }


        public void previous() {
            mMusicService.previous();
        }

        public void next() {
            mMusicService.next();
        }

        public void stopMusic() {
            mMusicService.stopMusic();
        }

        public int getCurTime(){
            //获取当前播放进度时间
            return mMusicService.getCurTime();
        }

        public int getDuration(){
            return mMusicService.getDuration();
        }

        /////////////////////
        //接口
        //返回当前播放歌曲

        public void setOnSongChangeListener(onSongChangeListener mListener){
            mSongChangeListener = mListener;
        }


    }



}

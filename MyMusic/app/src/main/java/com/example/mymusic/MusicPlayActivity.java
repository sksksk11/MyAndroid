package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mymusic.data.GlobalConstans;
import com.example.mymusic.data.Song;
import com.example.mymusic.service.MyMusicService;

import java.io.Serializable;
import java.util.ArrayList;

public class MusicPlayActivity extends AppCompatActivity {

    private ArrayList<Song> mSongArrayList;
    private int curSongIndex;
    private MyMusicService.myMusicBinder mBinder;
    private ImageView iv_palyorpause ;  //播放暂停按钮

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
           //服务已建立，传递数据（通过IBinder）
            mBinder = (MyMusicService.myMusicBinder) iBinder;
            mBinder.updateMusicList(mSongArrayList);
            mBinder.updateCurrentMusicIndex(curSongIndex);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        iv_palyorpause = findViewById(R.id.iv_palyorpause);  //暂定、播放按钮

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


    public void playOrPause(View view) {
        //播放或暂停
        if(mBinder.isPlaying()){
            //如果正在播放，则暂停播放
            mBinder.pause();
            Toast.makeText(this,"暂停播放",Toast.LENGTH_SHORT);
            iv_palyorpause.setImageResource(R.drawable.playpause);

        } else {
            //如果已暂停，则开始播放
            mBinder.startPlay();
            Toast.makeText(this,"暂停播放",Toast.LENGTH_SHORT);
            iv_palyorpause.setImageResource(R.drawable.img_play);
        }

    }


    public void playPrevious(View view) {
        mBinder.previous();

    }
}
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymusic.data.GlobalConstans;
import com.example.mymusic.data.Song;
import com.example.mymusic.myimplements.musicImpl;
import com.example.mymusic.myimplements.musicImpl.onSongChangeListener;
import com.example.mymusic.service.MyMusicService;
import com.example.mymusic.utils.timeTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayActivity extends AppCompatActivity {

    private ArrayList<Song> mSongArrayList;
    private int curSongIndex;
    private MyMusicService.myMusicBinder mBinder;
    private ImageView iv_palyorpause  ;  //播放暂停按钮
    private TextView tv_music_title ; //音乐标题
    private Song curSong ;  //当前播放歌曲
    private TextView tv_curTime,tv_duration ; //歌曲当前播放时间和总时间
    private SeekBar seekBar ; //进度条

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
           //服务已建立，传递数据（通过IBinder）
            mBinder = (MyMusicService.myMusicBinder) iBinder;
            mBinder.updateMusicList(mSongArrayList);
            mBinder.updateCurrentMusicIndex(curSongIndex);

            mBinder.setOnSongChangeListener(new onSongChangeListener() {
                @Override
                public void onSongChange(Song song) {
                    curSong = song;
                    tv_music_title.setText(curSong.getSongName());
                }
            });

            initUi();

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
        tv_music_title = findViewById(R.id.tv_music_title);  //音乐标题
        tv_curTime = findViewById(R.id.tv_cur_time);
        tv_duration = findViewById(R.id.tv_duration);
        seekBar = findViewById(R.id.seek_bar_music);

        Intent intent = getIntent();
        curSongIndex = intent.getIntExtra(GlobalConstans.KEY_SONG_INDEX,0);
//        mSongArrayList = (ArrayList<Song>) intent.getSerializableExtra(GlobalConstans.KEY_SONG_LIST);
        mSongArrayList = intent.getParcelableArrayListExtra(GlobalConstans.KEY_SONG_LIST);
        Log.d("tag", "当前歌曲序号: "+curSongIndex);
        if(mSongArrayList!=null){
            Log.d("tag", "歌曲信息: "+mSongArrayList);

            Song song = mSongArrayList.get(curSongIndex);
            tv_music_title.setText(song.getSongName());
        }

        startMusicService();

    }

    private void startMusicService() {
        Intent intent = new Intent(this, MyMusicService.class);
        bindService(intent,conn,BIND_AUTO_CREATE) ;

    }

    private void initUi(){
        //开启服务后
        //更新音乐播放进度时间
        int curTime = mBinder.getCurTime();
        tv_curTime.setText(timeTools.formateTime(curTime));
        seekBar.setProgress(curTime);

        //获取音乐总时长
        int durTime = mBinder.getDuration();
        tv_duration.setText(timeTools.formateTime(durTime));
        seekBar.setMax(durTime);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int curProgress = mBinder.getCurTime();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新进度条和当前播放进度时间
                        seekBar.setProgress(curProgress);
                        tv_curTime.setText(timeTools.formateTime(curProgress));
                    }
                });
            }
        },0,200);


    }


    public void playOrPause(View view) {
        //播放或暂停
        if(mBinder.isPlaying()){
            //如果正在播放，则暂停播放
            mBinder.pause();
            Toast.makeText(this,"暂停播放",Toast.LENGTH_SHORT);
            iv_palyorpause.setImageResource(R.drawable.img_play);

        } else {
            //如果已暂停，则开始播放
            mBinder.startPlay();
            Toast.makeText(this,"暂停播放",Toast.LENGTH_SHORT);
            iv_palyorpause.setImageResource(R.drawable.playpause);
        }

    }


    public void playPrevious(View view) {
        mBinder.previous();
        iv_palyorpause.setImageResource(R.drawable.playpause);

    }

    public void next(View view) {
        mBinder.next();
        iv_palyorpause.setImageResource(R.drawable.playpause);

        //获取音乐总时长
        int durTime = mBinder.getDuration();
        tv_duration.setText(timeTools.formateTime(durTime));
        seekBar.setMax(durTime);

    }


    public void stopMusic(View view) {
        mBinder.stopMusic();
        //更新播放按钮图标
        iv_palyorpause.setImageResource(R.drawable.img_play);
    }



}
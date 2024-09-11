package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mymusic.adapter.MySongListAdapter;
import com.example.mymusic.data.GlobalConstans;
import com.example.mymusic.data.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btn_test ,btn_frame ;
    private RecyclerView mRCVSongList;
    private MySongListAdapter mMySongListAdapter;
    private ArrayList<Song> mSongArrayList;
    private ImageView iv_bottom_icon;
    private int iv_clicked_times = 0;

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
        mSongArrayList.add(new Song("Obrayzow.mp3"));
//        mSongArrayList.add(new Song("Real People.mp3"));
//        mSongArrayList.add(new Song("Sul Sul.mp3"));
//        mSongArrayList.add(new Song("Test Card 4 4.mp3"));
//        mSongArrayList.add(new Song("The Sims 4 Theme.mp3"));

    }

    private void initSongList() {
        mMySongListAdapter = new MySongListAdapter(mSongArrayList,this);
        mMySongListAdapter.setItemClickListener(new MySongListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();

                //设置跳转，传参
                Intent intent = new Intent(MainActivity.this,MusicPlayActivity.class);
//                intent.putExtra(GlobalConstans.KEY_SONG_LIST,mSongArrayList);
                intent.putExtra(GlobalConstans.KEY_SONG_INDEX,position);
                intent.putParcelableArrayListExtra(GlobalConstans.KEY_SONG_LIST,mSongArrayList);

                startActivity(intent);
            }
        });
        mRCVSongList.setAdapter(mMySongListAdapter); //加载数据
        mRCVSongList.setLayoutManager(new LinearLayoutManager(this));  //设置布局

    }

    private void initView() {
        btn_test = (Button) findViewById(R.id.btn_test);
        btn_frame = (Button) findViewById(R.id.btn_frame);
        mRCVSongList = findViewById(R.id.rcv_song_lsit);
        iv_bottom_icon = findViewById(R.id.iv_bottom_icon);

        iv_bottom_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_clicked_times = iv_clicked_times +1;
                Log.d("TAG", "iv_clicked_times: "+iv_clicked_times);
                if(iv_clicked_times == 5){
                    btn_frame.setVisibility(View.VISIBLE);

                }else if(iv_clicked_times >= 7){
                    btn_frame.setVisibility(View.GONE);
                    iv_clicked_times = 0;
                }

            }
        });

    }


    public void toFramePage(View view) {
        Intent intent = new Intent(MainActivity.this,FrameActivity.class);
        startActivity(intent);

    }

    public void showPage(View view) {


    }

    @Override
    protected void onPause() {
        super.onPause();
        btn_frame.setVisibility(View.GONE);
    }
}
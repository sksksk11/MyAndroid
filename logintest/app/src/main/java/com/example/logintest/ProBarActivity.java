package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

public class ProBarActivity extends AppCompatActivity {

    private ProgressBar downlaodBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_bar);
        //初始化控件
        init();

        //启动进度条
        startDownloadBar();

    }

    private void startDownloadBar() {

        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i <100 ; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    downlaodBar.incrementProgressBy(1);
                    downlaodBar.incrementSecondaryProgressBy(2);
                }
            }
        }.start();

    }


    public void init(){
        downlaodBar = findViewById(R.id.pb_downloadbar);
        downlaodBar.setMax(100);
        downlaodBar.setProgress(20);

    }
}
package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private Button button_msg;
    private NewTimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化页面控件
        init();

        //点击返回按钮，返回登录页面
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击 发送短信按钮，10秒后才能再点击
        button_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_msg.setText("已发送");
//                button_msg.setBackgroundColor(0xFFFF00FF);
//                button_msg.setBackground(getResources().getDrawable(R.color.black));
                button_msg.setBackgroundColor(Color.parseColor("#f3f3f3"));
                button_msg.setTextColor(Color.parseColor("#333333"));
                button_msg.setClickable(false);

                timeCount.start();
            }
        });

    }

    public void init(){
        button_msg = findViewById(R.id.btn_msg);
        timeCount = new NewTimeCount(5*1000,1000);
    }


    private class NewTimeCount extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public NewTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            button_msg.setText("剩余"+millisUntilFinished/1000+"秒");
        }

        @Override
        public void onFinish() {
            button_msg.setText("重新发送");
//            button_msg.setBackgroundColor(Color.parseColor("#000000"));
            button_msg.setBackgroundColor(getResources().getColor(R.color.bgorange));
            button_msg.setTextColor(Color.parseColor("#ffffff"));
            button_msg.setClickable(true);

        }
    }
}
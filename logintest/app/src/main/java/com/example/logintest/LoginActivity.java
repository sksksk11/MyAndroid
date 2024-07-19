package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView et_register;
    private EditText userName;
    private TextView inputTip;
    private TimeCount mtimeCount;
    private Button btnTest,btnWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_register = findViewById(R.id.tv_register);
        et_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

//        输入用户名时，提示正在输入
        userName = findViewById(R.id.et_username);
        inputTip = findViewById(R.id.tv_inputTip);
        mtimeCount = new TimeCount(2*1000,100);
        btnTest = findViewById(R.id.btn_test);
        btnWebview = findViewById(R.id.btn_webview);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputTip.setText("正在输入用户名...");
                inputTip.setTextColor(Color.GREEN);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mtimeCount.cancel();
                mtimeCount.start();
            }
        });


        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RecViewActivity.class);
                startActivity(intent);
            }
        });

        btnWebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WebviewActivity.class);
                startActivity(intent);
            }
        });

    }


    class TimeCount extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            inputTip.setText("");
        }
    }

}
package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WebviewActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView wv_main;
    private TextView tv_main;
    private Button btn_go;
    private EditText et_url;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initData();

        btn_go.setOnClickListener(this);


    }


    //初始化页面数据
    public void initData() {

        tv_main = findViewById(R.id.tv_title);
        url = "http://www.163.com/";
        String texttmp = tv_main.getText().toString();
        tv_main.setText(texttmp+": "+url);
        btn_go = findViewById(R.id.btn_go);
        openUrl(url);


    }

    @Override
    public void onClick(View v) {

        EditText et_url = (EditText)findViewById(R.id.et_url);
        url = et_url.getText().toString();

        switch (v.getId()){
            case R.id.btn_go:
                Toast.makeText(this,"Go 按钮",Toast.LENGTH_SHORT).show();
                openUrl(url);
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
        //监听按键，按了返回键，回退页面，而不是关闭当前页面
        if(keyCode == KeyEvent.KEYCODE_BACK && wv_main.canGoBack()){
            wv_main.goBack();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //打开指定网址
    private void openUrl(String url) {

        url = url.trim();

        if(!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://"+url;
        }

        Toast.makeText(this,url,Toast.LENGTH_SHORT).show();
        // 创建 WebView 实例
        wv_main = findViewById(R.id.wv_main);
        // 设置 WebView 支持 JavaScript
        wv_main.getSettings().setJavaScriptEnabled(true);
        //设置在当前webview中显示
        wv_main.setWebViewClient(new WebViewClient(){
                                     @Override
                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                                         super.onReceivedError(view, errorCode, description, failingUrl);
                                         //处理 404 错误
//                                         if(errorCode == WebViewClient.ERROR_PAGE_NOT_FOUND){
//                                             wv_main.loadUrl("http://www.baidu.com");
//                                         }
                                     }
                                 }

        );
        // 加载网页
        wv_main.loadUrl(url);
    }
}
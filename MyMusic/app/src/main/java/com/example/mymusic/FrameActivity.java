package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FrameActivity extends AppCompatActivity {

    private Button btn_go,btn_collect,btn_bookMark;
    private EditText tv_url;
    private WebView wv_mainWebpage;
    private String loadedUrl ,webTitle ;  //存储页面加载后的网页url和标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        initView();



    }

    private void initView() {
        tv_url = findViewById(R.id.et_url);
        wv_mainWebpage = findViewById(R.id.wv_mainWebpage);

        String urlString = "http://www.163.com";

        tv_url.setText(urlString);
        //打开页面后自动隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        tv_url.clearFocus();

        //让WebView支持JavaScript脚本
        wv_mainWebpage.getSettings().setJavaScriptEnabled(true);
        //当需要从一个网页跳转到另一个网页时，目标网页仍然在当前WebView中显示，而不是打开系统浏览器。
        wv_mainWebpage.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadedUrl = view.getUrl();  //页面加载完成后，获取页面url
                tv_url.setText(loadedUrl);
                webTitle = view.getTitle();
//                Log.d("tag", "webTitle: "+webTitle);
            }



        });

        wv_mainWebpage.loadUrl(urlString);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (wv_mainWebpage.canGoBack()) {
                //如果webView可以返回上一页，则调用goBack()方法返回上一页
                wv_mainWebpage.goBack();
            } else {
                //否则，执行其他逻辑（比如退出Activity）
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
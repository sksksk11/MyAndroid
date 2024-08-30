package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymusic.utils.DatabaseHelper;
import com.example.mymusic.utils.UrlTools;

public class FrameActivity extends AppCompatActivity {

    private Button btn_go,btn_collect,btn_bookMark;
    private EditText tv_url;
    private WebView wv_mainWebpage;
    private String loadedUrl ,webTitle ;  //存储页面加载后的网页url和标题
    private DatabaseHelper mDatabaseHelper;
    private static final int REQUEST_CODE = 100 ;   //选择书签返回参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        initView();
        initDataBase();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭指针

        //关闭数据库
    }

    private void initDataBase() {
        //初始化数据库
        mDatabaseHelper = new DatabaseHelper(this);
    }

    private void initView() {
        tv_url = findViewById(R.id.et_url);
        wv_mainWebpage = findViewById(R.id.wv_mainWebpage);
        btn_collect = findViewById(R.id.btn_collect);

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

    public void addWeburl(View view) {
        //点击收藏按钮，新增网页收藏
        String url = tv_url.getText().toString().trim();
        String title = webTitle.trim();

        mDatabaseHelper.inserOneWebUrlByUrl(url,title);
        Toast.makeText(this,"已收藏："+title,Toast.LENGTH_SHORT).show();

    }

    public void openWebUrl(View view) {
        String url = tv_url.getText().toString().trim();
        Log.d("TAG", "inputUrl: "+url);
        if(url.length()==0){
            Toast.makeText(this,"url不能为空",Toast.LENGTH_SHORT).show();
        }else {
            //处理url字符串 ,加上http://
            url = UrlTools.formatUrl(url);
            Log.d("TAG", "formatUrl: "+url);
            wv_mainWebpage.loadUrl(url);
        }

    }

    public void browseSavedBookmark(View view) {
        Intent intent = new Intent(this,SavedBookmarkActivity.class);
//        startActivity(intent);
        startActivityForResult(intent,REQUEST_CODE);
    }
}
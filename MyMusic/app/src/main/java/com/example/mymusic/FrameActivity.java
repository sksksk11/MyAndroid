package com.example.mymusic;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mymusic.data.Keyword;
import com.example.mymusic.data.WebHistory;
import com.example.mymusic.data.WebInfo;
import com.example.mymusic.utils.DatabaseHelper;
import com.example.mymusic.utils.UrlTools;

public class FrameActivity extends AppCompatActivity {

    private Button btn_go,btn_collect,btn_bookMark;
    private EditText tv_url;
    private WebView wv_mainWebpage;
    private String loadedUrl ,webTitle ;  //存储页面加载后的网页url和标题
    private DatabaseHelper mDatabaseHelper;
    private static final int REQUEST_CODE = 100 ;   //选择书签返回参数
    private ActivityResultLauncher<Intent> activityResultLauncher ,activityResultHistory;
    private Context mContext;
    private LinearLayout ll_collectContainer,ll_clearContainer,ll_bookmarkContainer,ll_historyContainer,ll_keywordContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        initView();

        initDataBase();

        //获取返回值
        initGetResult();

    }



    private void initGetResult() {
        //获取书签页面选择的返回值
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d("TAG", "result: "+result.toString());
                        if(result.getResultCode() == RESULT_OK && result.getData()!=null){
                            WebInfo webInfo = (WebInfo) result.getData().getSerializableExtra("result");
                            String urlString = webInfo.getWebUrl();
                            Log.d("TAG", "已获取数据: "+webInfo.getWebTitle().toString());
                            tv_url.setText(urlString);
                            wv_mainWebpage.loadUrl(urlString);

                        }
                    }
                });

        //获取浏览历史页面选择的返回值
        activityResultHistory = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData()!=null) {
                    WebHistory webHistory = (WebHistory) result.getData().getSerializableExtra("result");

                    String urlString = webHistory.getWebUrl();

                    tv_url.setText(urlString);
                    wv_mainWebpage.loadUrl(urlString);
                }
            }
        });

        //复制关键词到剪贴板



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

        //判断数据库是否需要升级
        upgradeDatabase();
    }

    private void upgradeDatabase() {

        if (mDatabaseHelper != null) {
            SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
            int oldVersion = database.getVersion();
            Log.d("TAG", "oldVersion: "+oldVersion);
            int targetVersion  = mDatabaseHelper.DATABASE_NEW_VERSION;

            if(oldVersion<targetVersion ){
                database.beginTransaction();
                try {
                    mDatabaseHelper.onUpgrade(database,oldVersion,targetVersion);
                    database.setTransactionSuccessful();

                }catch (Exception e){
                 e.printStackTrace();
                }
                finally {
                    database.endTransaction();
                }
//                database.setVersion(targetVersion);

            }

            database.close();


        }

    }

    private void initView() {
        tv_url = findViewById(R.id.et_url);
        wv_mainWebpage = findViewById(R.id.wv_mainWebpage);
//        btn_collect = findViewById(R.id.btn_collect);    //原收藏按钮，已删除

        ll_collectContainer = findViewById(R.id.ll_collectContainer);
        ll_clearContainer = findViewById(R.id.ll_clearContainer);
        ll_bookmarkContainer = findViewById(R.id.ll_bookmarkContainer);
        ll_historyContainer = findViewById(R.id.ll_historyContainer);
        ll_keywordContainer = findViewById(R.id.ll_keywordContainer);

        mContext = this;

        String urlString = "http://www.163.com";

        tv_url.setText(urlString);
        //打开页面后自动隐藏输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        tv_url.clearFocus();

        //让WebView支持JavaScript脚本
        wv_mainWebpage.getSettings().setJavaScriptEnabled(true);
        wv_mainWebpage.getSettings().setDomStorageEnabled(true);
        wv_mainWebpage.getSettings().setMediaPlaybackRequiresUserGesture(false);  // 允许自动播放
        wv_mainWebpage.getSettings().setPluginState(WebSettings.PluginState.ON);

        //当需要从一个网页跳转到另一个网页时，目标网页仍然在当前WebView中显示，而不是打开系统浏览器。
        wv_mainWebpage.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String openningUrl = request.getUrl().toString();
                if(openningUrl.startsWith("http://") || openningUrl.startsWith("https://")){    //加载的url是http/https协议地址
                    tv_url.setText(openningUrl);
                    Toast.makeText(mContext,"正在打开"+openningUrl,Toast.LENGTH_SHORT).show();
                    wv_mainWebpage.loadUrl(openningUrl);
                    return false;  //返回false表示此url默认由系统处理,url未加载完成，会继续往下走
                }else{  //加载的url是自定义协议地址
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(openningUrl));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadedUrl = view.getUrl();  //页面加载完成后，获取页面url
                tv_url.setText(loadedUrl);
                webTitle = view.getTitle();

                //将地址和标题存入历史记录表
                mDatabaseHelper.saveToHistory(loadedUrl,webTitle,null);

            }
        });

//        wv_mainWebpage.loadUrl(urlString);


        //设置带返回参数的跳转activity，从书签页面返回？
//        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if(result.getResultCode()==RESULT_OK && result.getData()!=null){
//                            String urlData = result.getData().getStringExtra("result_url");
//                            tv_url.setText(urlData);
//                        }
//
//                    }
//                });



        //设置按钮点击事件
        //收藏按钮
        ll_collectContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv_url.getText().toString().isEmpty()) {
                    addWeburl(v);
                }
            }
        });

        //打开书签按钮
        ll_bookmarkContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseSavedBookmark(v);
            }
        });

        //清空地址栏按钮
        ll_clearContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_url.setText("");
                tv_url.requestFocus();
            }
        });

        //历史记录按钮
        ll_historyContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,HistoryActivity.class);
//                startActivity(intent);   //待改为取得返回参数的
                activityResultHistory.launch(intent);
            }
        });

        //关键词按钮
        ll_keywordContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KeywordActivity.class);
                startActivity(intent);
            }
        });

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
        if (tv_url.getText() != null && webTitle!=null) {
            String url = tv_url.getText().toString().trim();
            String title = webTitle.trim();

            mDatabaseHelper.inserOneWebUrlByUrl(url,title);
            Toast.makeText(this,"已收藏："+title,Toast.LENGTH_SHORT).show();
        }

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
        activityResultLauncher.launch(intent);

    }

    public void clearWebUrl(View view) {
        tv_url.setText("");
        tv_url.requestFocus();
    }
}
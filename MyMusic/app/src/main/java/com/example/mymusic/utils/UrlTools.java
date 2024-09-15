package com.example.mymusic.utils;

import android.util.Log;
import android.widget.Toast;

public class UrlTools {

    public static String formatUrl(String urlInput) {
        String url = urlInput.trim();

        //如果输入的地址未包含http:// 或者 https://，则自动补全
        if(url.contains("http://" ) ||  url.contains("https://")){
            Log.d("TAG", "url不变: ");
        }else {
            url = "http://" + url;
        }

        return url;
    }

    //传入web地址，解析出网页图标


}

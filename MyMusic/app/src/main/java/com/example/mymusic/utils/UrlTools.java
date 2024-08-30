package com.example.mymusic.utils;

import android.widget.Toast;

public class UrlTools {

    public static String formatUrl(String urlInput) {
        String url = urlInput.trim();

        //如果输入的地址未包含http:// 或者 https://，则自动补全
        if(!url.contains("http://" ) ||  !url.contains("https://")){
           url = "http://" + url;
        }

        return url;
    }

}

package com.example.mymusic.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class timeTools {

    public static String formateTime(int time){

        time = time / 1000 ;
        int minTime = time / 60 ;
        int secTime = time % 60 ;

        Log.d("tag", "minTime: "+minTime);
        Log.d("tag", "secTime: "+secTime);

        String minString =  minTime + "" ;
        if(minTime < 10){
            minString = "0"+minString ;
        }

        String secString =  secTime + "" ;
        if(secTime < 10){
            secString = "0"+secString ;
        }

        Log.d("tag", "minString: "+minString);
        Log.d("tag", "secString: "+secString);

        return minString +":"+secString;
    }

    //获取当前时间，并格式化为 YYYY-MM-DD HH:mm:ss
    public static String getCurrentTimeFormatted(){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        return sdf.format(new Date());
    }


}

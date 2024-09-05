package com.example.mymusic.utils;

import android.util.Log;

import com.example.mymusic.data.WebInfo;

import java.util.List;

public class ListTools {

    //复制一个书签列表的内容到另一个列表
    public static void copyList(List<WebInfo> sourceList,List<WebInfo> destinationList){
        if (sourceList != null) {
            for (WebInfo w: sourceList ) {
                destinationList.clear();
                destinationList.add(w);
            }
        }
    }

    //对比两个书签列表的书签顺序是否一致
    public static boolean compareListOrder(List<WebInfo> sourceList,List<WebInfo> destinationList){
        if (sourceList == null || destinationList == null) {
            return false;
        }

        if (sourceList.size() != destinationList.size()) {
            return false;
        }

        for (int i = 0; i < sourceList.size(); i++) {
            int sourceId = sourceList.get(i).getId();
            int destinationId = destinationList.get(i).getId();
            Log.d("TAG", "ID: "+sourceList.get(i).getId() + "——"+destinationList.get(i).getId() );
            if (sourceId!=destinationId) {  return false; }
        }
        return true;
    }


}

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


    //将书签移动到第X位，  n：第n位 ，-1：最后一位
    //传入item，和要放置的序号，N=N位，-1 = 末尾
    public static void changeBookmarkOrder(List<WebInfo> list,WebInfo webInfo,int newPosition){
        if( list !=null && webInfo != null && newPosition < list.size() ){

            if (list.indexOf(webInfo)==-1) { return;  }   // 不在列表内

            if (newPosition < -1) {
                //什么都不做
                return;
            }else if(newPosition == -1){
                //放置到最后一位
                int lastPosition = list.size()-1 ;
                list.add(lastPosition,list.remove(list.indexOf(webInfo)));
            }else if(newPosition >=0){
                //放置到n位
                list.add(newPosition,list.remove(list.indexOf(webInfo)));

            }



            }
        }

    }


    //将书签移动到列表最后一位




package com.example.mymusic.data;

import java.io.Serializable;

public class WebImage implements Serializable {
    int id;
    String imgUrl,imgName,collectionTime;
    boolean isDisplay;

    private WebImage(int id, String imgUrl, String imgName, String collectionTime, boolean isDisplay) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.imgName = imgName;
        this.collectionTime = collectionTime;
        this.isDisplay = isDisplay;
    }

    public WebImage(String imgUrl, String collectionTime, boolean isDisplay) {
        this.imgUrl = imgUrl;
        this.collectionTime = collectionTime;
        this.isDisplay = isDisplay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }
}

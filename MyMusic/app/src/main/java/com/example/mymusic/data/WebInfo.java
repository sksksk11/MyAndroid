package com.example.mymusic.data;

import com.example.mymusic.R;

public class WebInfo {
    private int number,icon;    //页面排序编码
    private String webUrl,webTitle,category;    //页面地址,标题,分类,图标

    public WebInfo(String webUrl, String webTitle) {
        this.webUrl = webUrl;
        this.webTitle = webTitle;
    }

    public WebInfo(String webUrl, String webTitle, int icon) {
        this.icon = icon;
        this.webUrl = webUrl;
        this.webTitle = webTitle;
    }

    public int getNumber() {
        return number;
    }

    private void setNumber(int number) {
        this.number = number;
    }

    public String getWebUrl() {
        return webUrl;
    }

    private void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebTitle() {
        return webTitle;
    }

    private void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getCategory() {
        return category;
    }

    private void setCategory(String category) {
        this.category = category;
    }

    public int getIcon() {
        return icon;
    }

    private void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "WebInfo{" +
                "number=" + number +
                ", icon=" + icon +
                ", webUrl='" + webUrl + '\'' +
                ", webTitle='" + webTitle + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}

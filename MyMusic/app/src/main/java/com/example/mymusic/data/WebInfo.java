package com.example.mymusic.data;

public class WebInfo {
    private int number;    //页面排序编码
    private String webUrl,webTitle,category,icon;    //页面地址,标题,分类,图标

    private WebInfo(String webUrl, String webTitle) {
        this.webUrl = webUrl;
        this.webTitle = webTitle;
    }

    private int getNumber() {
        return number;
    }

    private void setNumber(int number) {
        this.number = number;
    }

    private String getWebUrl() {
        return webUrl;
    }

    private void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    private String getWebTitle() {
        return webTitle;
    }

    private void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    private String getCategory() {
        return category;
    }

    private void setCategory(String category) {
        this.category = category;
    }

    private String getIcon() {
        return icon;
    }

    private void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "WebInfo{" +
                "number=" + number +
                ", webUrl='" + webUrl + '\'' +
                ", webTitle='" + webTitle + '\'' +
                ", category='" + category + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}

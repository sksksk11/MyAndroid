package com.example.mymusic.data;

import androidx.annotation.Nullable;

public class WebHistory {

    private int id;
    private String webUrl,webTitle,icon,visitTime;

    public WebHistory(int id, String webUrl, String webTitle, @Nullable String icon, @Nullable String visitTime) {
        this.id = id;
        this.webUrl = webUrl;
        this.webTitle = webTitle;
        this.icon = icon;
        this.visitTime = visitTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    @Override
    public String toString() {
        return "WebHistory{" +
                "id=" + id +
                ", webUrl='" + webUrl + '\'' +
                ", webTitle='" + webTitle + '\'' +
                ", icon='" + icon + '\'' +
                ", visitTime='" + visitTime + '\'' +
                '}';
    }
}

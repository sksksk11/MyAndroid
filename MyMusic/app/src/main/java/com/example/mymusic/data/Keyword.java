package com.example.mymusic.data;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Keyword implements Serializable {

    private int id,clicktimes ;
    private String keyword;
    private Integer icon ;

    public Keyword(int id, String keyword , int clicktimes , Integer icon) {
        this.id = id;
        this.clicktimes = clicktimes;
        this.keyword = keyword;
        this.icon = icon;
    }

    public Keyword(String keyword, @Nullable int clicktimes, @Nullable Integer icon) {

        this.clicktimes = clicktimes;
        this.keyword = keyword;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClicktimes() {
        return clicktimes;
    }

    public void setClicktimes(int clicktimes) {
        this.clicktimes = clicktimes;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "id=" + id +
                ", clicktimes=" + clicktimes +
                ", keyword='" + keyword + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}

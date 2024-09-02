package com.example.mymusic.data;

import com.example.mymusic.R;

import java.io.Serializable;

//用 Serializable 接口实现 序列化实例，将对象转换成可存储或可传输的状态
public class WebInfo implements Serializable {
    private int number,icon,id;    //页面排序编码
    private String webUrl,webTitle,category,is_del;    //页面地址,标题,分类,图标


    public WebInfo(String webUrl, String webTitle) {
        this.webUrl = webUrl;
        this.webTitle = webTitle;
    }

    public WebInfo(String webUrl, String webTitle, int icon,int id) {
        this.icon = icon;
        this.webUrl = webUrl;
        this.webTitle = webTitle;
        this.id  = id;
    }

    public String isIs_del() {     return is_del;   }
    public void setIs_del(String is_del) {      this.is_del = is_del;   }

    public int getId() {    return id;   }

    private void setId(int id) {   this.id = id;   }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "WebInfo{" +
                "number=" + number +
                ", icon=" + icon +
                ", id=" + id +
                ", webUrl='" + webUrl + '\'' +
                ", webTitle='" + webTitle + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}

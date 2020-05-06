package com.inhataxi.model;

import android.graphics.drawable.Drawable;

public class MainEventBanner {
    private String title;
    private String imgurl;
    private String url;

    public MainEventBanner(String title, String imgurl, String url) {
        this.title = title;
        this.imgurl = imgurl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

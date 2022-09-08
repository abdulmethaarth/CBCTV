package com.affinityopus.cbc_tv;

public class NewsData {

    private String title;
    private  String webpath;
    private String imageUrl;

    public NewsData(String title, String webpath, String imageUrl) {
        this.title = title;
        this.webpath = webpath;
        this.imageUrl = imageUrl;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebpath() {
        return webpath;
    }

    public void setWebpath(String webpath) {
        this.webpath = webpath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

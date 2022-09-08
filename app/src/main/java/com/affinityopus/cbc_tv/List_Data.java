package com.affinityopus.cbc_tv;

public class List_Data {
    private String name;
    private String moviename;
    private  String playlist;
    private String imageurl;
    private String addUrl;
    private  String addImage;


    public List_Data(String name,  String playlist, String imageurl, String addUrl, String addImage) {
        this.name = name;
         this.playlist = playlist;
        this.imageurl = imageurl;
        this.addUrl = addUrl;
        this.addImage = addImage;
    }

    public String getName() {
        return name;
    }



    public String getPlaylist() {
        return playlist;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getAddUrl() {
        return addUrl;
    }

    public String getAddImage() {
        return addImage;
    }
}
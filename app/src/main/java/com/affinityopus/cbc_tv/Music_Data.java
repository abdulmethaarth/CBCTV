package com.affinityopus.cbc_tv;

public class Music_Data {
    private String name;

    private  String playlist;
    private String movie;
    private  String imageUrl;

    public Music_Data(String name, String playlist, String movie, String imageUrl) {
        this.name = name;
        this.playlist = playlist;
        this.movie = movie;
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return name;
    }

    public String getPlaylist() {
        return playlist;
    }

    public String getMovie() {
        return movie;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
package com.affinityopus.cbc_tv;

public class Youtube_Data {
    private String video;

    private  String videoname;
    private  String imageUrl;

    public Youtube_Data(String video, String videoname, String imageUrl) {
        this.video = video;

        this.videoname = videoname;
        this.imageUrl = imageUrl;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }
}
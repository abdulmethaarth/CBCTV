package com.affinityopus.cbc_tv;

public class ScheduleData {

    private String title;
    private String imageUrl;

    public ScheduleData(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

package com.unacademyclone.model;

public class CollectionItem {
    private String uid, title, video_url;
    private int rank;
    private double duration;

    public CollectionItem(String uid, String title, String video_url, int rank, double duration) {
        this.uid = uid;
        this.title = title;
        this.video_url = video_url;
        this.rank = rank;
        this.duration = duration;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}

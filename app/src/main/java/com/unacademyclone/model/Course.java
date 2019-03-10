package com.unacademyclone.model;

public class Course {
    private String uid, name, language_display, thumbnail,concept_topology_title, authorName, authorAvatar;
    private int item_count, total_ratings;
    private double avg_rating;

    public Course(String uid, String name, String language_display, String thumbnail, String concept_topology_title, String authorName, String authorAvatar, int item_count, int total_ratings, double avg_rating) {
        this.uid = uid;
        this.name = name;
        this.language_display = language_display;
        this.thumbnail = thumbnail;
        this.concept_topology_title = concept_topology_title;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.item_count = item_count;
        this.total_ratings = total_ratings;
        this.avg_rating = avg_rating;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage_display() {
        return language_display;
    }

    public void setLanguage_display(String language_display) {
        this.language_display = language_display;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getConcept_topology_title() {
        return concept_topology_title;
    }

    public void setConcept_topology_title(String concept_topology_title) {
        this.concept_topology_title = concept_topology_title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public int getTotal_ratings() {
        return total_ratings;
    }

    public void setTotal_ratings(int total_ratings) {
        this.total_ratings = total_ratings;
    }

    public double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(double avg_rating) {
        this.avg_rating = avg_rating;
    }
}

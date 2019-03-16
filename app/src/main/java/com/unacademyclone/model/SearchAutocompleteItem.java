package com.unacademyclone.model;

public class SearchAutocompleteItem {
    private String type, title, user_name, avatar;
    private int followers, courses;

    public SearchAutocompleteItem(String type, String title, String user_name, String avatar, int followers, int courses) {
        this.type = type;
        this.title = title;
        this.user_name = user_name;
        this.avatar = avatar;
        this.followers = followers;
        this.courses = courses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getCourses() {
        return courses;
    }

    public void setCourses(int courses) {
        this.courses = courses;
    }
}

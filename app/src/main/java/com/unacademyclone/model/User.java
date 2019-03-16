package com.unacademyclone.model;

public class User {
    private String user_name, avatar, name, bio;
    private int followers_count, courses_count;

    public User(String user_name, String avatar, String name, String bio, int followers_count, int courses_count) {
        this.user_name = user_name;
        this.avatar = avatar;
        this.name = name;
        this.bio = bio;
        this.followers_count = followers_count;
        this.courses_count = courses_count;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getCourses_count() {
        return courses_count;
    }

    public void setCourses_count(int courses_count) {
        this.courses_count = courses_count;
    }
}

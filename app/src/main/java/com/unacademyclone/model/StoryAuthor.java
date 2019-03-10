package com.unacademyclone.model;

public class StoryAuthor {
    private String username, first_name, last_name, bio, uid, avatar,
            followers_count, follows_count,profile_since;
    private boolean is_verified_educator;

    public StoryAuthor(String username, String first_name, String last_name, String bio, String uid, String avatar, String followers_count, String follows_count, String profile_since, boolean is_verified_educator) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.bio = bio;
        this.uid = uid;
        this.avatar = avatar;
        this.followers_count = followers_count;
        this.follows_count = follows_count;
        this.profile_since = profile_since;
        this.is_verified_educator = is_verified_educator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(String followers_count) {
        this.followers_count = followers_count;
    }

    public String getFollows_count() {
        return follows_count;
    }

    public void setFollows_count(String follows_count) {
        this.follows_count = follows_count;
    }

    public String getProfile_since() {
        return profile_since;
    }

    public void setProfile_since(String profile_since) {
        this.profile_since = profile_since;
    }

    public boolean isIs_verified_educator() {
        return is_verified_educator;
    }

    public void setIs_verified_educator(boolean is_verified_educator) {
        this.is_verified_educator = is_verified_educator;
    }
}

package com.unacademyclone.model;

import java.util.List;

public class Story {

    //common attributes
    private String object_meta, message, uid, created_at, verb_text;
    private int object_type,reactions_count, comments_count, share_count, id;
    private boolean is_liked;
    private StoryAuthor storyAuthor;

    // object_type: 4,5
    private String language, collection_name, title, video_thumbnail;

    // object_type 4
    private int total_ratings;
    private double average_rating_star;
    private String course_author_name, course_author_avatar;

    // object_type 5
    private double video_duration;
    private int play_count;

    // object_type 8
    private List<Course> courseList;
    private String combo_name;

    public Story() {

    }

    public String getObject_meta() {

        return object_meta;
    }

    public void setObject_meta(String object_meta) {
        this.object_meta = object_meta;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVerb_text() {
        return verb_text;
    }

    public void setVerb_text(String verb_text) {
        this.verb_text = verb_text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getObject_type() {
        return object_type;
    }

    public void setObject_type(int object_type) {
        this.object_type = object_type;
    }

    public int getReactions_count() {
        return reactions_count;
    }

    public void setReactions_count(int reactions_count) {
        this.reactions_count = reactions_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public boolean isIs_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

    public int getTotal_ratings() {
        return total_ratings;
    }

    public void setTotal_ratings(int total_ratings) {
        this.total_ratings = total_ratings;
    }

    public double getAverage_rating_star() {
        return average_rating_star;
    }

    public void setAverage_rating_star(double average_rating_star) {
        this.average_rating_star = average_rating_star;
    }

    public double getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(double video_duration) {
        this.video_duration = video_duration;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public StoryAuthor getStoryAuthor() {
        return storyAuthor;
    }

    public void setStoryAuthor(StoryAuthor storyAuthor) {
        this.storyAuthor = storyAuthor;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public String getCollection_name() {
        return collection_name;
    }

    public void setCollection_name(String collection_name) {
        this.collection_name = collection_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourse_author_name() {
        return course_author_name;
    }

    public void setCourse_author_name(String course_author_name) {
        this.course_author_name = course_author_name;
    }

    public String getCourse_author_avatar() {
        return course_author_avatar;
    }

    public void setCourse_author_avatar(String course_author_avatar) {
        this.course_author_avatar = course_author_avatar;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getCombo_name() {
        return combo_name;
    }

    public void setCombo_name(String combo_name) {
        this.combo_name = combo_name;
    }
}


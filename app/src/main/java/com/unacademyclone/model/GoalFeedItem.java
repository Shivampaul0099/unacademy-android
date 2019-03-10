package com.unacademyclone.model;

import java.util.List;

public class GoalFeedItem {


    private String type, title;

    private List<Educator> educatorList;
    private List<AllTopicsItem> allTopicsItemList;
    private List<TopicGroupItem> topicGroupItemList;

    public GoalFeedItem(String type, String title){
        this.type = type;
        this.title = title;
    }

    public List<Educator> getEducatorList() {
        return educatorList;
    }

    public void setEducatorList(List<Educator> educatorList) {
        this.educatorList = educatorList;
    }

    public List<AllTopicsItem> getAllTopicsItemList() {
        return allTopicsItemList;
    }

    public void setAllTopicsItemList(List<AllTopicsItem> allTopicsItemList) {
        this.allTopicsItemList = allTopicsItemList;
    }

    public List<TopicGroupItem> getTopicGroupItemList() {
        return topicGroupItemList;
    }

    public void setTopicGroupItemList(List<TopicGroupItem> topicGroupItemList) {
        this.topicGroupItemList = topicGroupItemList;
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
}

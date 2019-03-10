package com.unacademyclone.model;

public class Goal {
    private String uid, name, icon_url;

    public Goal(String uid, String name, String icon_url) {
        this.uid = uid;
        this.name = name;
        this.icon_url = icon_url;
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

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}

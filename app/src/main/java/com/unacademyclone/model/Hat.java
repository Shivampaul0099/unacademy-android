package com.unacademyclone.model;

public class Hat {
    private String hat_icon;
    private int count;
    private boolean showCount;

    public Hat(String hat_icon, int count, boolean showCount) {
        this.hat_icon = hat_icon;
        this.count = count;
        this.showCount = showCount;
    }

    public String getHat_icon() {
        return hat_icon;
    }

    public void setHat_icon(String hat_icon) {
        this.hat_icon = hat_icon;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isShowCount() {
        return showCount;
    }

    public void setShowCount(boolean showCount) {
        this.showCount = showCount;
    }
}

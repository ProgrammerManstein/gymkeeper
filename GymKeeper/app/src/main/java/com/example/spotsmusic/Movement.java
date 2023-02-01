package com.example.spotsmusic;

public class Movement {
    String title;
    String tab;
    int time;
    int group;
    int ca;
    String type;
    public Movement(String title,String tab, int time,int group, int ca ,String type){
        this.title=title;
        this.tab=tab;
        this.time=time;
        this.group=group;
        this.type=type;
        this.ca=ca;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCa() {
        return ca;
    }

    public void setCa(int ca) {
        this.ca = ca;
    }
}

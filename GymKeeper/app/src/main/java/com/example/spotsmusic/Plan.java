package com.example.spotsmusic;

import java.util.Calendar;

public class Plan {
    Calendar cal;
    String content;
    String type;
    public Plan(String content,Calendar cal,String type) {
        this.content=content;
        this.cal=cal;
        this.type=type;
    }

    public Calendar getCal() {
        return cal;
    }
    public String getContent(){
        return content;
    }
    public String getType(){return type;}

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type){this.type=type;}
}

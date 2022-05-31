package edu.zjut.zzy.ticklist.bean;

import java.sql.Date;
import java.sql.Time;

public class Focus {
    private int kilinUserId;
    private int groupId;
    private String content;
    private String description;
    private Date date;
    private int focusTime;
    private Time time;

    public Focus(int kilinUserId, int groupId, String content, String description, Date date, int focusTime, Time time) {
        this.kilinUserId = kilinUserId;
        this.groupId = groupId;
        this.content = content;
        this.description = description;
        this.date = date;
        this.focusTime = focusTime;
        this.time = time;
    }

    public int getKilinUserId() {
        return kilinUserId;
    }

    public void setKilinUserId(int kilinUserId) {
        this.kilinUserId = kilinUserId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(int focusTime) {
        this.focusTime = focusTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}

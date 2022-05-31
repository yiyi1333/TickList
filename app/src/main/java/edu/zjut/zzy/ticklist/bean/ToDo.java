package edu.zjut.zzy.ticklist.bean;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ToDo implements Cloneable{
    //id
    private int kiLinId;

    //待办内容
    private String content;
    //待办描述
    private String description;
    //待办日期
    private LocalDate date;
    //重复间隔
    private int repeatedWay;
    //重复组号
    private int groupId;
    //不重复
    public static final int NOREPEATED = 0;
    //每天
    public static final int DAYLY = 1;
    //每周
    public static final int WEEKLY = 7;

    //提醒时间
    private LocalDateTime time;

    //计时方式
    private int timerMode;

    //正向计时
    public static final int POSITIVE = 1;
    //倒计时
    public static final int PASSIVE = -1;

    //目标时间
    private int targetTime;
    //已经完成时间
    private int finishTime;
    //完成状态
    private boolean isFinish;

    //番茄钟时间
    private int focusTime;

    public ToDo() {
    }

    @NonNull
    @Override
    public ToDo clone(){
        ToDo newtoDo = null;
        try {
            newtoDo = (ToDo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assert newtoDo != null;
        return newtoDo;
    }

    public ToDo(int kiLinId, String content, String description, LocalDate date, int repeatedWay, int groupId, LocalDateTime time, int timerMode, int targetTime, int finishTime, boolean isFinish, int focusTime) {
        this.kiLinId = kiLinId;
        this.content = content;
        this.description = description;
        this.date = date;
        this.repeatedWay = repeatedWay;
        this.groupId = groupId;
        this.time = time;
        this.timerMode = timerMode;
        this.targetTime = targetTime;
        this.finishTime = finishTime;
        this.isFinish = isFinish;
        this.focusTime = focusTime;
    }

    public ToDo(String content, String description, LocalDate date, int repeatedWay, LocalDateTime time, int timerMode, int targetTime, int finishTime, boolean isFinish, int focusTime) {
        this.content = content;
        this.description = description;
        this.date = date;
        this.repeatedWay = repeatedWay;
        this.time = time;
        this.timerMode = timerMode;
        this.targetTime = targetTime;
        this.finishTime = finishTime;
        this.isFinish = isFinish;
        this.focusTime = focusTime;
    }

    public int getKiLinId() {
        return kiLinId;
    }

    public void setKiLinId(int kiLinId) {
        this.kiLinId = kiLinId;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRepeatedWay() {
        return repeatedWay;
    }

    public void setRepeatedWay(int repeatedWay) {
        this.repeatedWay = repeatedWay;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getTimerMode() {
        return timerMode;
    }

    public void setTimerMode(int timerMode) {
        this.timerMode = timerMode;
    }

    public int getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(int targetTime) {
        this.targetTime = targetTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public int getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(int focusTime) {
        this.focusTime = focusTime;
    }
}

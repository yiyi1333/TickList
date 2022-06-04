package edu.zjut.zzy.ticklist.bean;

import java.util.Arrays;

public class Course {
    //移动应用开发
    private String courseName;
    //1-8周
    private int beginWeek;
    private int endWeek;
    //郁文楼B305
    private String courseClassroom;
    //李伟
    private String courseTeacher;
    //1-2节 {1, 2}
    private int beginTime;
    private int endTime;

    //Monday
    private int courseWeekday;

    @Override
    public String toString() {
        return "Course{" +
                "courseName='" + courseName + '\'' +
                ", beginWeek=" + beginWeek +
                ", endWeek=" + endWeek +
                ", courseClassroom='" + courseClassroom + '\'' +
                ", courseTeacher='" + courseTeacher + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", courseWeekday='" + courseWeekday + '\'' +
                '}';
    }

    public Course(String[] infoArrays, int weekDay) {
        //课程名
        this.courseName = infoArrays[0];
        //移动应用开发 (1-2节)1-8周
        String [] timeweekCuts = infoArrays[1].split("\\)");
        //(1-2节 1-8周
        String [] timeCuts = timeweekCuts[0].split("-");
        StringBuilder time = new StringBuilder();
        for(int i = 0; i < timeCuts[0].length(); i++){
            if(timeCuts[0].charAt(i) >= '0' && timeCuts[0].charAt(i) <= '9'){
                time.append(timeCuts[0].charAt(i));
            }
        }
        this.beginTime = Integer.parseInt(time.toString());
        time = new StringBuilder();
        for(int i = 0; i < timeCuts[1].length(); i++){
            if(timeCuts[1].charAt(i) >= '0' && timeCuts[1].charAt(i) <= '9'){
                time.append(timeCuts[1].charAt(i));
            }
        }
        this.endTime = Integer.parseInt(time.toString());

        String [] weekCuts = timeweekCuts[1].split("-");
        StringBuilder week = new StringBuilder();
        for(int i = 0; i < weekCuts[0].length(); i++){
            if(weekCuts[0].charAt(i) >= '0' && weekCuts[0].charAt(i) <= '9'){
                week.append(weekCuts[0].charAt(i));
            }
        }
        this.beginWeek = Integer.parseInt(week.toString());
        week = new StringBuilder();
        for(int i = 0; i < weekCuts[1].length(); i++){
            if(weekCuts[1].charAt(i) >= '0' && weekCuts[1].charAt(i) <= '9'){
                week.append(weekCuts[1].charAt(i));
            }
        }
        this.endWeek = Integer.parseInt(week.toString());
        this.courseClassroom = infoArrays[2] + " " + infoArrays[3];
        this.courseTeacher = infoArrays[4];
        this.courseWeekday = weekDay;
    }


    public int getCourseWeekday() {
        return courseWeekday;
    }

    public void setCourseWeekday(int courseWeekday) {
        this.courseWeekday = courseWeekday;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseClassroom() {
        return courseClassroom;
    }

    public void setCourseClassroom(String courseClassroom) {
        this.courseClassroom = courseClassroom;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public int getBeginWeek() {
        return beginWeek;
    }

    public void setBeginWeek(int beginWeek) {
        this.beginWeek = beginWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}

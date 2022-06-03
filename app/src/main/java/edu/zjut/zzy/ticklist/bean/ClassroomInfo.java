package edu.zjut.zzy.ticklist.bean;

public class ClassroomInfo {
    private String classroomName;
    private String roomCode;
    private int number;
    private int rank;

    @Override
    public String toString() {
        return "ClassroomInfo{" +
                "classroomName='" + classroomName + '\'' +
                ", roomCode='" + roomCode + '\'' +
                ", number=" + number +
                ", rank=" + rank +
                '}';
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

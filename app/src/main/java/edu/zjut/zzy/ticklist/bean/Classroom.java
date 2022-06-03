package edu.zjut.zzy.ticklist.bean;

public class Classroom {
    private String classroomName;
    private String roomCode;

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

    public Classroom(String classroomName, String roomCode) {
        this.classroomName = classroomName;
        this.roomCode = roomCode;
    }
}

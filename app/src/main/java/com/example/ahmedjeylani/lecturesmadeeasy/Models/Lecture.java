package com.example.ahmedjeylani.lecturesmadeeasy.Models;

import java.io.Serializable;

public class Lecture implements Serializable{

    private String uniqueID, lectureRoom, lectureTitle, professor, courseName, date, lectureStatus;

    public Lecture (){}

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getLectureRoom() {
        return lectureRoom;
    }

    public void setLectureRoom(String lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    public String getLectureTitle() {
        return lectureTitle;
    }

    public void setLectureTitle(String lectureTitle) {
        this.lectureTitle = lectureTitle;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLectureStatus() {
        return lectureStatus;
    }

    public void setLectureStatus(String lectureStatus) {
        this.lectureStatus = lectureStatus;
    }
}

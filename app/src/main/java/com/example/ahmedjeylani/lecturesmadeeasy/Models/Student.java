package com.example.ahmedjeylani.lecturesmadeeasy.Models;

import java.io.Serializable;

public class Student extends BaseUser implements Serializable {

    String studentId;

    public Student(){}

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}

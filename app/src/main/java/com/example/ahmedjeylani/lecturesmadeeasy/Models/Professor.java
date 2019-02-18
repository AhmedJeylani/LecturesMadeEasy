package com.example.ahmedjeylani.lecturesmadeeasy.Models;

import java.io.Serializable;

public class Professor extends BaseUser implements Serializable {

    String bio;

    public Professor(){}

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}

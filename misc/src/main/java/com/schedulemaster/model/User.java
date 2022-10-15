package com.schedulemaster.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String id;
    private String pw;

    public ArrayList<Lecture> selectedLectures;
    public ArrayList<Lecture> enrolledLectures;
}

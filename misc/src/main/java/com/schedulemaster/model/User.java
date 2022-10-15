package com.schedulemaster.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String id;
    private String pw;

    public ArrayList<Lecture> selectedLectures; // 책가방 강의
    public ArrayList<Lecture> enrolledLectures; // 신청 완료된 강의
}

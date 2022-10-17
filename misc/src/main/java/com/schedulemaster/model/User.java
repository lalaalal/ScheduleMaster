package com.schedulemaster.model;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.util.SHA256;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class User implements Serializable {
    public static final long serialVersionUID = 2L;

    public String id;
    private String pw;

    public LinkedList<Lecture> selectedLectures = new LinkedList<>(); // 책가방 강의
    public LinkedList<Lecture> enrolledLectures = new LinkedList<>(); // 신청 완료된 강의

    public User(String id, String pw) {
        this.id = id;
        this.pw = SHA256.encrypt(pw);
    }

    public boolean verifyPassword(String hashedPassword) {
        return pw.equals(hashedPassword);
    }

    public void enrollLecture(Lecture lecture) {
        enrolledLectures.push(lecture);
    }

    public void selectLecture(Lecture lecture) {
        selectedLectures.push(lecture);
    }

    public void cancelLecture(Lecture lecture) {
        enrolledLectures.remove(lecture);
    }

    public void unselectLecture(Lecture lecture) {
        selectedLectures.remove(lecture);
    }
}

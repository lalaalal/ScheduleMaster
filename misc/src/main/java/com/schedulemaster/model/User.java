package com.schedulemaster.model;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;

import java.io.Serializable;

public class User implements Serializable {
    public static final long serialVersionUID = 20L;

    public final String id;
    private final String hashedPassword;

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        if (1 <= grade && grade <= 4)
            this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    private int grade = 1;
    private String major = "교필";

    public final LinkedList<String> selectedLectures = new LinkedList<>(); // 책가방 강의
    public final LinkedList<String> enrolledLectures = new LinkedList<>(); // 신청 완료된 강의
    public Hash<String, Integer> priorities = new Hash<>();
    public LectureTime unwantedTime = new LectureTime();

    public User(String id, String hashedPassword) {
        this.id = id;
        this.hashedPassword = hashedPassword;
    }

    public boolean verifyPassword(String hashedPassword) {
        return this.hashedPassword.equals(hashedPassword);
    }

    public void enrollLecture(Lecture lecture) {
        enrolledLectures.push(lecture.lectureNum);
        lecture.enrolled += 1;
    }

    public void selectLecture(Lecture lecture) {
        selectedLectures.push(lecture.lectureNum);
    }

    public void cancelLecture(Lecture lecture) {
        enrolledLectures.remove(lecture.lectureNum);
        lecture.enrolled -= 1;
    }

    public void unselectLecture(Lecture lecture) {
        selectedLectures.remove(lecture.lectureNum);
    }

    public void addUnwantedTime(LectureTime.TimeSet timeSet) {
        unwantedTime.addTimeSet(timeSet);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pw='" + hashedPassword + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        return hashedPassword.equals(user.hashedPassword);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + hashedPassword.hashCode();
        return result;
    }
}

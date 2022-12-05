package com.schedulemaster.model;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;

import java.io.Serializable;

/**
 * User class. Password should be hashed.
 *
 * @author lalaalal
 */
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

    /**
     * Create a new User.
     *
     * @param id             User id.
     * @param hashedPassword Password that is hashed.
     */
    public User(String id, String hashedPassword) {
        this.id = id;
        this.hashedPassword = hashedPassword;
    }

    /**
     * Compare hashed password.
     *
     * @param hashedPassword Hashed password to compare.
     * @return True if password matches.
     */
    public boolean verifyPassword(String hashedPassword) {
        return this.hashedPassword.equals(hashedPassword);
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

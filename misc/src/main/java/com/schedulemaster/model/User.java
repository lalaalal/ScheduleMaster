package com.schedulemaster.model;

import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;

import java.io.Serializable;

public class User implements Serializable {
    public static final long serialVersionUID = 12L;

    public final String id;
    private final String hashedPassword;

    public final LinkedList<Lecture> selectedLectures = new LinkedList<>(); // 책가방 강의
    public final LinkedList<Lecture> enrolledLectures = new LinkedList<>(); // 신청 완료된 강의
    public final Heap<Priority> priorities = new Heap<>(Heap.Comparator.maxHeapInt(Priority::priority));
    public final LectureTime unwantedTime = new LectureTime();

    public User(String id, String hashedPassword) {
        this.id = id;
        this.hashedPassword = hashedPassword;
    }

    public boolean verifyPassword(String hashedPassword) {
        return this.hashedPassword.equals(hashedPassword);
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

package com.schedulemaster.app.controller;

import com.schedulemaster.app.Client;
import com.schedulemaster.app.ResponseStatus;
import com.schedulemaster.app.Subject;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.misc.Request;
import com.schedulemaster.model.*;

import java.io.IOException;

public class UserController extends Subject {
    private User user;
    private final Client client;
    private final LectureBook lectureBook;

    public UserController(Client client, LectureBook lectureBook) {
        this.client = client;
        this.lectureBook = lectureBook;
    }

    public User getUser() {
        return user;
    }

    public ResponseStatus login(String id, String pw) throws IOException {
        ResponseStatus status = client.login(id, pw);
        if (status.status())
            user = client.getUserData();

        return status;
    }

    public void logout() {
        user = null;
    }

    public ResponseStatus signup(String id, String pw, String major, int grade) throws IOException {
        return client.signup(id, pw, major, grade);
    }

    public ResponseStatus enrollLecture(Lecture lecture) throws IOException {
        ResponseStatus status = client.lectureCommand(Request.ENROLL, lecture);
        refresh();
        return status;
    }

    public ResponseStatus cancelLecture(Lecture lecture) throws IOException {
        ResponseStatus status = client.lectureCommand(Request.CANCEL, lecture);
        refresh();
        return status;
    }

    public ResponseStatus selectLecture(Lecture lecture) throws IOException {
        ResponseStatus status = client.lectureCommand(Request.SELECT, lecture);
        refresh();
        return status;
    }

    public ResponseStatus unselectLecture(Lecture lecture) throws IOException {
        ResponseStatus status = client.lectureCommand(Request.UNSELECT, lecture);
        refresh();
        return status;
    }

    public LinkedList<Lecture> getEnrolledLectures() {
        LinkedList<Lecture> enrolledLectures = new LinkedList<>();
        for (String lectureNum : user.enrolledLectures) {
            Lecture lecture = lectureBook.findLecture(lectureNum);
            if (lecture != null)
                enrolledLectures.push(lecture);
        }
        return enrolledLectures;
    }

    public LinkedList<Lecture> getSelectedLectures() {
        LinkedList<Lecture> selectedLectures = new LinkedList<>();
        for (String lectureNum : user.selectedLectures) {
            Lecture lecture = lectureBook.findLecture(lectureNum);
            if (lecture != null)
                selectedLectures.push(lecture);
        }
        return selectedLectures;
    }

    public boolean savePriorities(Hash<String, Integer> priorities) throws IOException {
        user.priorities = priorities;
        return client.sendPriorities(priorities);
    }

    public Heap<Priority> getPriorityHeap() {
        LectureGroup group = new LectureGroup();
        group.addAll(user.priorities.getKeys());
        return group.createHeap(user.priorities);
    }

    public void addUnwantedTime(LectureTime.TimeSet timeSet) {
        user.unwantedTime.addTimeSet(timeSet);
    }

    public void setUnwantedTime(LectureTime unwantedTime) {
        user.unwantedTime = unwantedTime;
    }

    public boolean saveUnwantedTime() throws IOException {
        return client.sendUnwantedTime(user.unwantedTime);
    }

    public LectureTime getUnwantedTime() {
        return user.unwantedTime;
    }

    public int getUserGrade() {
        return user.getGrade();
    }

    public String getUserMajor() {
        return user.getMajor();
    }

    public void refresh() throws IOException {
        if (user != null)
            user = client.getUserData();

        notice();
    }
}

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

    public UserController(Client client) {
        this.client = client;
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

    public ResponseStatus signup(String id, String pw) throws IOException {
        return client.signup(id, pw);
    }

    public ResponseStatus enrollLecture(Lecture lecture) throws IOException {
        ResponseStatus status = client.lectureCommand(Request.ENROLL, lecture);
        refresh();
        if (status.status())
            user.enrollLecture(lecture);
        return status;
    }

    public ResponseStatus cancelLecture(Lecture lecture) throws IOException {
        ResponseStatus status = client.lectureCommand(Request.CANCEL, lecture);
        refresh();
        if (status.status())
            user.cancelLecture(lecture);
        return status;
    }

    public ResponseStatus selectLecture(Lecture lecture) throws IOException {
        ResponseStatus status = client.lectureCommand(Request.SELECT, lecture);
        refresh();
        if (status.status())
            user.selectLecture(lecture);
        return status;
    }

    public ResponseStatus unselectLecture(Lecture lecture) throws IOException {
        ResponseStatus status = client.lectureCommand(Request.UNSELECT, lecture);
        refresh();
        if (status.status())
            user.unselectLecture(lecture);
        return status;
    }

    public LinkedList<Lecture> getEnrolledLectures() {
        return user.enrolledLectures;
    }

    public LinkedList<Lecture> getSelectedLectures() {
        return user.selectedLectures;
    }

    public boolean savePriorities(Hash<Lecture, Integer> priorities) throws IOException {
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

    public void refresh() throws IOException {
        if (user != null)
            user = client.getUserData();
        notice();
    }
}

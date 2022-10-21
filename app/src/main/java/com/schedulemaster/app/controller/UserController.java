package com.schedulemaster.app.controller;

import com.schedulemaster.app.Client;
import com.schedulemaster.app.LoginStatus;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.Request;
import com.schedulemaster.model.*;

import java.io.IOException;

public class UserController {
    private User user;
    private final Client client;

    public UserController(Client client) {
        this.client = client;
    }

    public LoginStatus login(String id, String pw) throws IOException {
        LoginStatus status = client.login(id, pw);
        if (status.status())
            user = client.getUserData();

        return status;
    }

    public LoginStatus signup(String id, String pw) throws IOException {
        return client.signup(id, pw);
    }

    public boolean enrollLecture(Lecture lecture) throws IOException {
        boolean result = client.lectureCommand(Request.ENROLL, lecture);
        if (result)
            user.enrollLecture(lecture);
        return result;
    }

    public boolean cancelLecture(Lecture lecture) throws IOException {
        boolean result = client.lectureCommand(Request.CANCEL, lecture);
        if (result)
            user.cancelLecture(lecture);
        return result;
    }

    public boolean selectLecture(Lecture lecture) throws IOException {
        boolean result = client.lectureCommand(Request.SELECT, lecture);
        if (result)
            user.selectLecture(lecture);
        return result;
    }

    public boolean unselectLecture(Lecture lecture) throws IOException {
        boolean result = client.lectureCommand(Request.UNSELECT, lecture);
        if (result)
            user.unselectLecture(lecture);
        return result;
    }

    public Lecture[] getEnrolledLectures() {
        return user.enrolledLectures.toArray(new Lecture[0]);
    }

    public Lecture[] getSelectedLectures() {
        return user.selectedLectures.toArray(new Lecture[0]);
    }

    public void savePriorities(Hash<Lecture, Integer> priorities) throws IOException {
        user.priorities = priorities;
        client.sendPriorities(priorities);
    }

    public Heap<Priority> getPriorityHeap() {
        LectureGroup group = new LectureGroup();
        group.addAll(user.priorities.getKeys());
        return group.createHeap(user.priorities);
    }

    public void addUnwantedTime(LectureTime.TimeSet timeSet) {
        user.unwantedTime.addTimeSet(timeSet);
    }

    public void saveUnwantedTime() throws IOException {
        client.sendUnwantedTime(user.unwantedTime);
    }

    public LectureTime getUnwantedTime() {
        return user.unwantedTime;
    }
}

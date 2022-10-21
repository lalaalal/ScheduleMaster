package com.schedulemaster.app.controller;

import com.schedulemaster.app.Client;
import com.schedulemaster.app.LoginStatus;
import com.schedulemaster.misc.Request;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.User;

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
}

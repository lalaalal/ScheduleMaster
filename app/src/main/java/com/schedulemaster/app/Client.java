package com.schedulemaster.app;

import com.schedulemaster.misc.*;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.User;
import com.schedulemaster.util.SHA256;
import com.schedulemaster.util.SerializeManager;

import java.io.*;
import java.net.Socket;

public class Client extends Communicator {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5678;

    public Client() throws IOException {
        super(new Socket(HOST, PORT));
    }

    public boolean login(String id, String pw) throws IOException {
        String hashedPassword = SHA256.encrypt(pw);

        String[] userInfo = new String[2];
        userInfo[0] = id;
        userInfo[1] = hashedPassword;

        Request request = new Request(Request.LOGIN, userInfo);
        Response response = send(request);
        return response != null && response.status() == Status.SUCCEED;
    }

    @SuppressWarnings("unchecked")
    public LinkedList<Lecture> getLectures() throws IOException {
        Request request = new Request(Request.REQ_LECTURES, null);
        Response response = send(request);

        return (LinkedList<Lecture>) response.data();
    }

    public User getUserData() {
        throw new UnsupportedOperationException();
    }

    public boolean enrollLecture(Lecture lecture, User user) {
        throw new UnsupportedOperationException();
    }

    public boolean selectLecture(Lecture lecture, User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Response createResponse(Request request) {
        throw new UnsupportedOperationException();
    }
}

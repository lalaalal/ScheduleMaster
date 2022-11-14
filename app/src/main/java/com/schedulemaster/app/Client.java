package com.schedulemaster.app;

import com.schedulemaster.misc.*;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;
import com.schedulemaster.model.User;
import com.schedulemaster.util.SHA256;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

/**
 * Client communicate with server. Default host is "aws.lalaalal.com".
 *
 * @author lalaalal
 */
public class Client extends Communicator {
    private static final String HOST = Objects.requireNonNullElse(System.getenv("CUSTOM_HOST"), "aws.lalaalal.com");
    private static final int PORT = 5678;

    public Client() throws IOException {
        super(new Socket(HOST, PORT));
    }

    /**
     * Login.
     *
     * @param id User id.
     * @param pw Raw user password (Not hashed).
     * @return Status of login.
     * @throws IOException If an I/O error occurs.
     */
    public ResponseStatus login(String id, String pw) throws IOException {
        String hashedPassword = SHA256.encrypt(pw);

        String[] userInfo = new String[2];
        userInfo[0] = id;
        userInfo[1] = hashedPassword;

        Request request = new Request(Request.LOGIN, userInfo);
        Response response = send(request);

        return new ResponseStatus(response.status() == Status.SUCCEED, (String) response.data());
    }

    /**
     * Sign up.
     *
     * @param id New id.
     * @param pw New pw (Not hashed).
     * @return Status of signup.
     * @throws IOException If an I/O error occurs.
     */
    public ResponseStatus signup(String id, String pw) throws IOException {
        String hashedPassword = SHA256.encrypt(pw);

        String[] userInfo = new String[2];
        userInfo[0] = id;
        userInfo[1] = hashedPassword;

        Request request = new Request(Request.SIGNUP, userInfo);
        Response response = send(request);

        return new ResponseStatus(response.status() == Status.SUCCEED, (String) response.data());
    }

    /**
     * Get all lectures from server.
     * @return List of lectures.
     * @throws IOException If an I/O error occurs.
     */
    @SuppressWarnings("unchecked")
    public LinkedList<Lecture> getLectures() throws IOException {
        Request request = new Request(Request.REQ_LECTURES, null);
        Response response = send(request);

        return (LinkedList<Lecture>) response.data();
    }

    /**
     * Get user data from server.
     * @return User data.
     * @throws IOException If an I/O error occurs.
     */
    public User getUserData() throws IOException {
        Request request = new Request(Request.REQ_USER, null);
        Response response = send(request);

        return (User) response.data();
    }

    /**
     * Do lectures command (enroll, cancel, select, unselect).
     *
     * @param command Lecture command.
     * @param lecture Lecture to be handled.
     * @return ResponseStatus of command.
     * @throws IOException If an I/O error occurs.
     */
    public ResponseStatus lectureCommand(String command, Lecture lecture) throws IOException {
        Request request = new Request(command, lecture);
        Response response = send(request);
        if (response == null)
            return new ResponseStatus(false, "");
        return new ResponseStatus(response.status() == Status.SUCCEED, response.data().toString());
    }

    /**
     * Save user priorities of lectures at server.
     * @param priorities Priorities of lectures.
     * @return True if succeeded.
     * @throws IOException If an I/O error occurs.
     */
    public boolean sendPriorities(Hash<String, Integer> priorities) throws IOException {
        Request request = new Request(Request.SET_PRIORITIES, priorities);
        Response response = send(request);

        return response != null && response.status() == Status.SUCCEED;
    }

    /**
     * Save unwanted time to server.
     * @param unwantedTime Unwanted time.
     * @return True if succeeded.
     * @throws IOException If an I/O error occurs.
     */
    public boolean sendUnwantedTime(LectureTime unwantedTime) throws IOException {
        Request request = new Request(Request.SET_UNWANTED_TIME, unwantedTime);
        Response response = send(request);

        return response != null && response.status() == Status.SUCCEED;
    }

    private void bye() throws IOException {
        Request request = new Request(Request.BYE, null);
        Response response = send(request);

        if (response.status() != Status.BYE)
            throw new IOException("Bye was not succeed");
    }

    @Override
    public void close() throws IOException {
        bye();
        super.close();
    }

    /**
     * Client does not support handling request. Only request is allowed.
     */
    @Override
    protected Response createResponse(Request request) {
        throw new UnsupportedOperationException();
    }
}

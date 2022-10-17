package com.schedulemaster.app;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.User;

import java.io.*;
import java.net.Socket;

public class Client implements AutoCloseable {
    private static final String HOST = "localhost";
    private static final int PORT = 5678;
    private final Socket socket;

    public Client() throws IOException {
        socket = new Socket(HOST, PORT);
    }

    public boolean login(String id, String pw) {
        throw new UnsupportedOperationException();
    }

    public LinkedList<Lecture> getLectures() {
        throw new UnsupportedOperationException();
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

    public static <T> T deserializeObject(byte[] bytes, Class<T> type) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                return type.cast(ois.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }
}

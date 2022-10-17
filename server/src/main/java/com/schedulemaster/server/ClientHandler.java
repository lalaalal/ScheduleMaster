package com.schedulemaster.server;

import com.schedulemaster.misc.Request;
import com.schedulemaster.misc.Response;
import com.schedulemaster.model.User;
import com.schedulemaster.util.SerializeManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable, AutoCloseable {
    private final Socket client;
    private final BufferedInputStream inputStream;
    private final BufferedOutputStream outputStream;

    private final LectureHandler lectureHandler;

    private final ResponseFactory factory = new ResponseFactory();

    private User user;

    public ClientHandler(Socket client, LectureHandler lectureHandler) throws IOException {
        this.client = client;
        this.inputStream = new BufferedInputStream(client.getInputStream());
        this.outputStream = new BufferedOutputStream(client.getOutputStream());
        this.lectureHandler = lectureHandler;
    }

    @Override
    public void run() {

    }

    private void process() throws IOException, ClassNotFoundException {
        byte[] bytes = inputStream.readAllBytes();
        Request request = SerializeManager.deserialize(bytes, Request.class);

        Response response = factory.createResponse(request);

    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        client.close();
    }

    private class ResponseFactory {
        public Response createResponse(Request request) {
            throw new UnsupportedOperationException();
        }

        public Response loginResponse(Request request) {
            throw new UnsupportedOperationException();
        }

        public static Response lectureResponse(Request request) {
            throw new UnsupportedOperationException();
        }
    }
}

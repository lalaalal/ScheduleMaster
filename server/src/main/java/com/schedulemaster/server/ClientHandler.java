package com.schedulemaster.server;

import com.schedulemaster.misc.Request;
import com.schedulemaster.misc.Response;
import com.schedulemaster.misc.Status;
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
    private final UserHandler userHandler;

    private final ResponseFactory factory = new ResponseFactory();

    private User user;

    public ClientHandler(Socket client, LectureHandler lectureHandler, UserHandler userHandler) throws IOException {
        this.client = client;
        this.inputStream = new BufferedInputStream(client.getInputStream());
        this.outputStream = new BufferedOutputStream(client.getOutputStream());
        this.lectureHandler = lectureHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void run() {
        Status status = Status.SUCCEED;
        while (status != Status.BYE) {
            try {
                status = process();
            } catch (IOException e) {
                System.out.println("Something went wrong while handling client request");
                return;
            }
        }
    }

    private Status process() throws IOException {
        byte[] requestBytes = inputStream.readAllBytes();
        Request request = SerializeManager.deserialize(requestBytes, Request.class);

        Response response = factory.createResponse(request);
        byte[] responseBytes = SerializeManager.serialize(response);

        outputStream.write(responseBytes);

        return response.status();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        client.close();
    }

    private class ResponseFactory {
        public Response createResponse(Request request) {
            if (request == null)
                return commandNotFoundResponse();
            return switch (request.command()) {
                case Request.LOGIN -> loginResponse(request);
                case Request.REQ_LECTURES -> lectureResponse();
                default -> commandNotFoundResponse();
            };
        }

        public Response loginResponse(Request request) {
            String[] userInfo = (String[]) request.data();
            String id = userInfo[0];
            String hashedPassword = userInfo[1];

            Status status = Status.FAILED;
            if (userHandler.verifyUser(id, hashedPassword)) {
                status = Status.SUCCEED;
                user = userHandler.getUser(id);
            }

            return new Response(status, null);
        }

        public Response lectureResponse() {
            return new Response(Status.SUCCEED, lectureHandler.getLectures());
        }

        public Response commandNotFoundResponse() {
            return new Response(Status.FAILED, null);
        }
    }
}

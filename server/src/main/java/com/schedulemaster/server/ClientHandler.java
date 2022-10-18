package com.schedulemaster.server;

import com.schedulemaster.misc.Communicator;
import com.schedulemaster.misc.Request;
import com.schedulemaster.misc.Response;
import com.schedulemaster.misc.Status;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.User;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Communicator implements Runnable {

    private final LectureHandler lectureHandler;
    private final UserHandler userHandler;

    private final ResponseFactory factory = new ResponseFactory();

    private User user;

    public ClientHandler(Socket client, LectureHandler lectureHandler, UserHandler userHandler) throws IOException {
        super(client);
        this.lectureHandler = lectureHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void run() {
        Status status = Status.SUCCEED;
        while (status != Status.BYE) {
            try {
                status = receiveAndSend();
            } catch (IOException e) {
                System.out.println("Something went wrong while handling client request");
            }
        }
    }

    @Override
    protected Response createResponse(Request request) {
        return factory.createResponse(request);
    }

    private class ResponseFactory {
        public Response createResponse(Request request) {
            if (request == null)
                return commandNotFoundResponse();
            return switch (request.command()) {
                case Request.LOGIN -> loginResponse(request);
                case Request.SIGNUP -> signupResponse(request);
                case Request.REQ_LECTURES -> lectureResponse();
                case Request.ENROLL, Request.SELECT,
                        Request.CANCEL, Request.UNSELECT -> lectureCommandResponse(request);
                case Request.BYE -> byeResponse();
                default -> commandNotFoundResponse();
            };
        }

        public Response loginResponse(Request request) {
            String[] userInfo = (String[]) request.data();
            String id = userInfo[0];
            String hashedPassword = userInfo[1];

            if (userHandler.verifyUser(id, hashedPassword)) {
                user = userHandler.getUser(id);
                return new Response(Status.SUCCEED, "Succeed");
            }
            if (userHandler.hasId(id))
                return new Response(Status.FAILED, "Wrong Password");

            return new Response(Status.FAILED, "ID not found");
        }

        public synchronized Response signupResponse(Request request) {
            String[] userInfo = (String[]) request.data();
            String id = userInfo[0];
            String hashedPassword = userInfo[1];

            if (userHandler.hasId(id))
                return new Response(Status.FAILED, "Id exists");

            userHandler.addUser(id, hashedPassword);
            return new Response(Status.SUCCEED, "Succeed");
        }

        public Response lectureResponse() {
            return new Response(Status.SUCCEED, lectureHandler.getLectures());
        }

        public synchronized Response lectureCommandResponse(Request request) {
            Lecture lecture = (Lecture) request.data();
            if (!lectureHandler.doLectureCommand(request.command(), lecture, user))
                return new Response(Status.FAILED, null);

            userHandler.save();
            return new Response(Status.SUCCEED, null);
        }

        public Response byeResponse() {
            return new Response(Status.BYE, null);
        }

        public Response commandNotFoundResponse() {
            return new Response(Status.FAILED, null);
        }
    }
}

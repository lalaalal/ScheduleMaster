package com.schedulemaster.server;

import com.schedulemaster.misc.*;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;
import com.schedulemaster.model.User;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Communicator implements Runnable {

    private final LectureHandler lectureHandler;
    private final UserHandler userHandler;

    private final ResponseFactory factory = new ResponseFactory();

    private User user;
    private final Logger logger = Logger.getInstance();
    private final String id;

    public ClientHandler(int id, Socket client, LectureHandler lectureHandler, UserHandler userHandler) throws IOException {
        super(client);
        this.lectureHandler = lectureHandler;
        this.userHandler = userHandler;
        this.id = "Client-" + id;
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
        logger.log("Bye bye", Logger.INFO, id);
    }

    @Override
    protected Response createResponse(Request request) {
        logger.log("Received request (" + request.command() + ")", Logger.DEBUG, id);
        return factory.createResponse(request);
    }

    private class ResponseFactory {
        public Response createResponse(Request request) {
            if (request == null)
                return commandNotFoundResponse();
            Response response = switch (request.command()) {
                case Request.LOGIN -> loginResponse(request);
                case Request.SIGNUP -> signupResponse(request);
                case Request.REQ_USER -> userDataResponse();
                case Request.REQ_LECTURES -> lectureResponse();
                case Request.ENROLL, Request.SELECT,
                        Request.CANCEL, Request.UNSELECT -> lectureCommandResponse(request);
                case Request.SET_PRIORITIES -> setPrioritiesResponse(request);
                case Request.SET_UNWANTED_TIME -> setUnwantedTimeResponse(request);
                case Request.BYE -> byeResponse();
                default -> commandNotFoundResponse();
            };

            logger.log("Create response (" + response + ")", Logger.DEBUG, id);
            return response;
        }

        public Response loginResponse(Request request) {
            if (!(request.data() instanceof String[] userInfo))
                return new Response(Status.FAILED, "Wrong Request");
            String id = userInfo[0];
            String hashedPassword = userInfo[1];

            if (userHandler.verifyUser(id, hashedPassword)) {
                user = userHandler.getUser(id);
                logger.log("\"" + id + "\" login succeed", Logger.INFO, id);
                return new Response(Status.SUCCEED, "Succeed");
            }
            logger.log("\"" + id + "\" login failed", Logger.INFO, id);
            if (userHandler.hasId(id))
                return new Response(Status.FAILED, "Wrong Password");

            return new Response(Status.FAILED, "ID not found");
        }

        public synchronized Response signupResponse(Request request) {
            if (!(request.data() instanceof String[] userInfo))
                return new Response(Status.FAILED, "Wrong Request");

            String id = userInfo[0];
            String hashedPassword = userInfo[1];

            if (userHandler.hasId(id)) {
                logger.log("\"" + id + "\" signup failed", Logger.INFO, id);
                return new Response(Status.FAILED, "Id exists");
            }

            userHandler.addUser(id, hashedPassword);
            logger.log("\"" + id + "\" signup succeed", Logger.INFO, id);
            return new Response(Status.SUCCEED, "Succeed");
        }

        public Response userDataResponse() {
            if (user == null)
                return new Response(Status.FAILED, null);
            return new Response(Status.SUCCEED, user);
        }

        public Response lectureResponse() {
            return new Response(Status.SUCCEED, lectureHandler.getLectures());
        }

        public synchronized Response lectureCommandResponse(Request request) {
            if (!(request.data() instanceof Lecture lecture))
                return new Response(Status.FAILED, "Wrong Request");
            if (!lectureHandler.doLectureCommand(request.command(), lecture.lectureNum, user))
                return new Response(Status.FAILED, null);

            userHandler.save();
            return new Response(Status.SUCCEED, null);
        }

        @SuppressWarnings("unchecked")
        public synchronized Response setPrioritiesResponse(Request request) {
            if (!(request.data() instanceof Hash<?, ?> priorities))
                return new Response(Status.FAILED, "Wrong Request");

            logger.log("Update \"" + user.id + "\"'s priorities", Logger.DEBUG, id);
            user.priorities = (Hash<Lecture, Integer>) priorities;
            userHandler.save();

            return new Response(Status.SUCCEED, null);
        }

        public synchronized Response setUnwantedTimeResponse(Request request) {
            if (!(request.data() instanceof LectureTime unwantedTime))
                return new Response(Status.FAILED, "Wrong Request");

            logger.log("Update \"" + user.id + "\"'s unwanted time", Logger.DEBUG, id);
            user.unwantedTime = unwantedTime;
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

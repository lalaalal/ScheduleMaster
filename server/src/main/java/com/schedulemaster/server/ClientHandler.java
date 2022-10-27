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

    public ClientHandler(Socket client, LectureHandler lectureHandler, UserHandler userHandler) throws IOException {
        super(client);
        this.lectureHandler = lectureHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void run() {
        try {
            Status status = Status.SUCCEED;
            while (status != Status.BYE) {
                status = receiveAndSend();
            }
        } catch (IOException e) {
            logger.log(e.getMessage(), Logger.ERROR);
        }
        if (user != null) {
            userHandler.logout(user);
            logger.log("Logout", Logger.INFO);
        }

        logger.log("Bye bye", Logger.INFO);
        logger.log("", Logger.INFO);
    }

    @Override
    protected Response createResponse(Request request) {
        logger.log("Request.command : " + request.command(), Logger.DEBUG);
        logger.log("Request.data : " + request.getDataType(), Logger.VERBOSE);
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

            logger.log("Response.status : " + response.status(), Logger.DEBUG);
            logger.log("Response.data : "  + response.getDataType(), Logger.VERBOSE);
            logger.log("", Logger.DEBUG);
            return response;
        }

        public Response loginResponse(Request request) {
            if (!(request.data() instanceof String[] userInfo))
                return new Response(Status.FAILED, Response.WRONG_REQUEST);
            String id = userInfo[0];
            String hashedPassword = userInfo[1];

            if (userHandler.verifyUser(id, hashedPassword)) {
                user = userHandler.getUser(id);
                if (!userHandler.login(user)) {
                    logger.log("\"" + id + "\" already has login session", Logger.INFO);
                    user = null;
                    return new Response(Status.FAILED, "session_exists");
                }

                logger.log("\"" + id + "\" login succeed", Logger.INFO);
                return new Response(Status.SUCCEED, Response.SUCCEED);
            }
            logger.log("\"" + id + "\" login failed", Logger.INFO);
            if (userHandler.hasId(id))
                return new Response(Status.FAILED, "wrong_password");

            return new Response(Status.FAILED, "id_not_found");
        }

        public Response signupResponse(Request request) {
            if (!(request.data() instanceof String[] userInfo))
                return new Response(Status.FAILED, Response.WRONG_REQUEST);

            String id = userInfo[0];
            String hashedPassword = userInfo[1];

            if (id.length() == 0)
                return new Response(Status.FAILED, "id_is_empty");

            if (userHandler.hasId(id)) {
                logger.log("\"" + id + "\" signup failed", Logger.INFO);
                return new Response(Status.FAILED, "id_exists");
            }

            userHandler.addUser(id, hashedPassword);
            logger.log("\"" + id + "\" signup succeed", Logger.INFO);
            return new Response(Status.SUCCEED, Response.SUCCEED);
        }

        public Response userDataResponse() {
            if (user == null)
                return new Response(Status.FAILED, Response.FAILED);
            return new Response(Status.SUCCEED, user);
        }

        public Response lectureResponse() {
            return new Response(Status.SUCCEED, lectureHandler.getLectures());
        }

        public Response lectureCommandResponse(Request request) {
            if (user == null)
                return new Response(Status.FAILED, Response.LOGIN_REQUIRED);

            if (!(request.data() instanceof Lecture lecture))
                return new Response(Status.FAILED, Response.WRONG_REQUEST);

            String result = lectureHandler.doLectureCommand(request.command(), lecture.lectureNum, user);
            if (!result.equals(Response.SUCCEED))
                return new Response(Status.FAILED, result);

            userHandler.save();
            return new Response(Status.SUCCEED, Response.SUCCEED);
        }

        @SuppressWarnings("unchecked")
        public synchronized Response setPrioritiesResponse(Request request) {
            if (user == null)
                return new Response(Status.FAILED, Response.LOGIN_REQUIRED);

            if (!(request.data() instanceof Hash<?, ?> priorities))
                return new Response(Status.FAILED, Response.WRONG_REQUEST);

            logger.log("Update \"" + user.id + "\"'s priorities", Logger.DEBUG);
            user.priorities = (Hash<Lecture, Integer>) priorities;
            userHandler.save();

            return new Response(Status.SUCCEED, Response.SUCCEED);
        }

        public synchronized Response setUnwantedTimeResponse(Request request) {
            if (user == null)
                return new Response(Status.FAILED, Response.LOGIN_REQUIRED);

            if (!(request.data() instanceof LectureTime unwantedTime))
                return new Response(Status.FAILED, Response.WRONG_REQUEST);

            logger.log("Update \"" + user.id + "\"'s unwanted time", Logger.DEBUG);
            user.unwantedTime = unwantedTime;
            userHandler.save();

            return new Response(Status.SUCCEED, Response.SUCCEED);
        }

        public Response byeResponse() {
            return new Response(Status.BYE, Response.SUCCEED);
        }

        public Response commandNotFoundResponse() {
            return new Response(Status.FAILED, Response.WRONG_REQUEST);
        }
    }
}

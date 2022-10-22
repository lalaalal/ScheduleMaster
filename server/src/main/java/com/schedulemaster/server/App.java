package com.schedulemaster.server;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static final String DEFAULT_LECTURE_FILE_PATH = "lectures";
    public static final String DEFAULT_USER_FILE_PATH = "users";

    private final LectureHandler lectureHandler;
    private final UserHandler userHandler;

    Logger logger = Logger.getInstance();

    public static void main(String[] args) {
        App app = new App(args);
        app.startServer();
    }
    public App(String[] args) {
        logger.addOutputStream(System.out);

        String lectureFilePath = DEFAULT_LECTURE_FILE_PATH;
        String userFilePath = DEFAULT_USER_FILE_PATH;

        if (args.length >= 1)
            lectureFilePath = args[0];
        if (args.length >= 2)
            userFilePath = args[1];

        lectureHandler = new LectureHandler(lectureFilePath);
        userHandler = new UserHandler(userFilePath);
    }

    public void startServer() {
        try (Server server = new Server(lectureHandler, userHandler);
             Scanner scanner = new Scanner(System.in)) {
            Thread serverThread = new Thread(server);
            serverThread.start();

            String command = "";
            while (!command.equals("exit")) {
                command = scanner.next();
                handleCommand(command, scanner);
            }

        } catch (IOException e) {
            logger.log(e.getMessage(), Logger.ERROR);
        }
    }

    private void handleCommand(String command, Scanner scanner) {
        
    }
}

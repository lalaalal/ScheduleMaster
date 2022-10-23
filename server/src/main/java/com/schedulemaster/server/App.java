package com.schedulemaster.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App implements AutoCloseable {
    public static final String DEFAULT_LECTURE_FILE_PATH = "lectures";
    public static final String DEFAULT_USER_FILE_PATH = "users";

    private final LectureHandler lectureHandler;
    private final UserHandler userHandler;
    private FileOutputStream logFileOutputStream = null;

    Logger logger = Logger.getInstance();

    public static void main(String[] args) {
        try (App app = new App(args)) {
            app.startServer();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    public App(String[] args) throws IOException {
        logger.addOutputStream(System.out);

        String lectureFilePath = DEFAULT_LECTURE_FILE_PATH;
        String userFilePath = DEFAULT_USER_FILE_PATH;

        if (args.length >= 1)
            lectureFilePath = args[0];
        if (args.length >= 2)
            userFilePath = args[1];
        if (args.length >= 3 && !args[2].equals("no-save-log")) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            String fileName = formatter.format(now) + ".log";
            logFileOutputStream = new FileOutputStream(Paths.get(args[2], fileName).toFile());
            logger.addOutputStream(logFileOutputStream);
        }


        lectureHandler = new LectureHandler(lectureFilePath);
        userHandler = new UserHandler(userFilePath);
    }

    public void startServer() {
        try (Server server = new Server(lectureHandler, userHandler);
             Scanner scanner = new Scanner(System.in)) {
            Thread serverThread = new Thread(server);
            serverThread.setName("Server");
            serverThread.start();

            String command = "";
            while (!isExitCommand(command)) {
                command = scanner.next();
                handleCommand(command, scanner);
            }

        } catch (IOException e) {
            logger.log(e.getMessage(), Logger.ERROR);
        }
    }

    private void handleCommand(String command, Scanner scanner) {
        switch (command) {
            case "append_csv" -> {
                String path = scanner.next();
                lectureHandler.appendFromCSV(path);
            }
            case "log_level" -> {
                int logLevel = scanner.nextInt();
                logger.setLogLevel(logLevel);
                logger.log("Set LOG_LEVEL to " + logLevel, Logger.INFO);
            }
            case "exit", "stop" -> logger.log("Stopping Server", Logger.INFO);
            default -> {
                logger.log("help", Logger.INFO);
                logger.log(":\tappend_csv [path]", Logger.INFO);
                logger.log(":\tlog_level [0 (ERROR) | 1 (INFO) | 2 (DEBUG) | 3 (VERBOSE)]", Logger.INFO);
                logger.log(":\tstop | exit", Logger.INFO);
            }
        }
    }

    private boolean isExitCommand(String command) {
        return command.equals("exit") || command.equals("stop");
    }

    @Override
    public void close() throws IOException {
        if (logFileOutputStream != null)
            logFileOutputStream.close();
    }
}

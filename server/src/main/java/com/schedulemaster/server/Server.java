package com.schedulemaster.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements AutoCloseable, Runnable {
    private static final int PORT = 5678;

    private final ServerSocket serverSocket;
    private final LectureHandler lectureHandler;
    private final UserHandler userHandler;
    private final Logger logger = Logger.getInstance();

    public Server(LectureHandler lectureHandler, UserHandler userHandler) throws IOException {
        serverSocket = new ServerSocket(PORT);
        this.lectureHandler = lectureHandler;
        this.userHandler = userHandler;
        logger.addOutputStream(System.out);
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }

    @Override
    public void run() {
        logger.log("Starting server", Logger.INFO);
        while (true) {
            try {
                Socket client = serverSocket.accept();
                logger.log("New connection from " + client.getInetAddress(), Logger.INFO);
                ClientHandler clientHandler = new ClientHandler(client, lectureHandler, userHandler);
                Thread clientHandlerThread = new Thread(clientHandler);
                clientHandlerThread.start();
            } catch (IOException e) {
                logger.log(e.getMessage(), Logger.ERROR);
            }
        }
    }
}

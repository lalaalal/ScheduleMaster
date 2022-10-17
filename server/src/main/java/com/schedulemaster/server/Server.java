package com.schedulemaster.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements AutoCloseable, Runnable {
    private static final int PORT = 5678;

    private final ServerSocket serverSocket;
    private final LectureHandler lectureHandler;
    private final UserHandler userHandler;

    public Server(LectureHandler lectureHandler, UserHandler userHandler) throws IOException {
        serverSocket = new ServerSocket(PORT);
        this.lectureHandler = lectureHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client, lectureHandler, userHandler);
                Thread clientHandlerThread = new Thread(clientHandler);
                clientHandlerThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.schedulemaster.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server implements AutoCloseable, Runnable {
    private static final int PORT = 5678;

    private final ServerSocket serverSocket;
    private final LectureHandler lectureHandler;
    private final UserHandler userHandler;
    private final Logger logger = Logger.getInstance();

    private boolean run = true;

    public Server(LectureHandler lectureHandler, UserHandler userHandler) throws IOException {
        serverSocket = new ServerSocket(PORT);
        this.lectureHandler = lectureHandler;
        this.userHandler = userHandler;
    }

    @Override
    public void close() throws IOException {
        run = false;
        serverSocket.close();
    }

    @Override
    public void run() {
        logger.log("Starting server", Logger.INFO);
        int clientId = 0;
        while (run) {
            try {
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client, lectureHandler, userHandler);
                Thread clientHandlerThread = new Thread(clientHandler);
                clientHandlerThread.setName("Client-" + clientId);
                logger.log("New connection from " + client.getInetAddress()
                        + " -> " + clientHandlerThread.getName(), Logger.INFO);
                clientHandlerThread.start();
                clientId += 1;
            } catch (SocketException e) {
                logger.log(e.getMessage(), Logger.INFO);
            } catch (IOException e) {
                logger.log(e.getMessage(), Logger.ERROR);
            }
        }
    }
}

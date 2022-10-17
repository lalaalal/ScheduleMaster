package com.schedulemaster.misc;

import com.schedulemaster.util.SerializeManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Communicator implements AutoCloseable {
    private final Socket socket;
    private final BufferedInputStream inputStream;
    private final BufferedOutputStream outputStream;

    public Communicator(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new BufferedInputStream(socket.getInputStream());
        this.outputStream = new BufferedOutputStream(socket.getOutputStream());
    }

    public Response send(Request request) throws IOException {
        byte[] requestBytes = SerializeManager.serialize(request);
        outputStream.write(requestBytes);

        byte[] responseBytes = inputStream.readAllBytes();
        return SerializeManager.deserialize(responseBytes, Response.class);
    }

    public Status receiveAndSend() throws IOException {
        byte[] requestBytes = inputStream.readAllBytes();
        Request request = SerializeManager.deserialize(requestBytes, Request.class);

        Response response = createResponse(request);
        byte[] responseBytes = SerializeManager.serialize(response);
        outputStream.write(responseBytes);

        return response.status();
    }

    protected abstract Response createResponse(Request request);

    @Override
    public void close() throws Exception {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}

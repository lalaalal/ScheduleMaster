package com.schedulemaster.misc;

import com.schedulemaster.util.SerializeManager;

import java.io.*;
import java.net.Socket;

public abstract class Communicator implements AutoCloseable {
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public Communicator(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    public Response send(Request request) throws IOException {
        byte[] requestBytes = SerializeManager.serialize(request);
        outputStream.write(requestBytes);
        outputStream.flush();

        byte[] responseBytes = receiveBytes();
        return SerializeManager.deserialize(responseBytes, Response.class);
    }

    public Status receiveAndSend() throws IOException {
        byte[] requestBytes = receiveBytes();
        Request request = SerializeManager.deserialize(requestBytes, Request.class);

        Response response = createResponse(request);
        byte[] responseBytes = SerializeManager.serialize(response);
        outputStream.write(responseBytes);
        outputStream.flush();

        return response.status();
    }

    public byte[] receiveBytes() throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int nRead = 0;
            byte[] receivedBytes = new byte[10240];

            nRead = inputStream.read(receivedBytes, 0, receivedBytes.length);
            buffer.write(receivedBytes, 0, nRead);
            while (inputStream.available() > 0) {
                nRead = inputStream.read(receivedBytes, 0, receivedBytes.length);
                buffer.write(receivedBytes, 0, nRead);
            }

            return buffer.toByteArray();
        }
    }

    protected abstract Response createResponse(Request request);

    @Override
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}

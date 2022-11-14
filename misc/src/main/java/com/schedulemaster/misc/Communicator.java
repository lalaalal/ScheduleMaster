package com.schedulemaster.misc;

import com.schedulemaster.util.SerializeManager;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Abstract class for communication between schedule master server and client.
 * Provides send and receive protocol.
 *
 * @author lalaalal
 */
public abstract class Communicator implements AutoCloseable {
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    /**
     * Initialize with socket.
     *
     * @param socket Socket.
     * @throws IOException If getting streams is not fine.
     */
    public Communicator(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    /**
     * Send request.
     *
     * @param request Request.
     * @return Response for request.
     * @throws IOException If an I/O error occurs.
     */
    public Response send(Request request) throws IOException {
        byte[] requestBytes = SerializeManager.serialize(request);
        byte[] lengthByte = ByteBuffer.allocate(Integer.BYTES).putInt(requestBytes.length).array();
        outputStream.write(lengthByte);
        outputStream.write(requestBytes);
        outputStream.flush();

        byte[] responseBytes = receiveBytes();
        return SerializeManager.deserialize(responseBytes, Response.class);
    }

    /**
     * Receive request and handle. And send response.
     * @return Response for request.
     * @throws IOException If an I/O error occurs.
     */
    public Status receiveAndSend() throws IOException {
        byte[] requestBytes = receiveBytes();
        Request request = SerializeManager.deserialize(requestBytes, Request.class);

        Response response = createResponse(request);
        byte[] responseBytes = SerializeManager.serialize(response);
        byte[] lengthByte = ByteBuffer.allocate(Integer.BYTES).putInt(responseBytes.length).array();
        outputStream.write(lengthByte);
        outputStream.write(responseBytes);
        outputStream.flush();

        return response.status();
    }

    /**
     * Receive bytes from socket.
     *
     * @return received bytes.
     * @throws IOException If an I/O error occurs.
     */
    public byte[] receiveBytes() throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] lengthByte = inputStream.readNBytes(Integer.BYTES);
            ByteBuffer wrapped = ByteBuffer.wrap(lengthByte);
            int length = wrapped.getInt();

            int nRead;
            int allRead = 0;
            byte[] receivedBytes = new byte[10240];

            nRead = inputStream.read(receivedBytes, 0, receivedBytes.length);
            allRead += nRead;
            if (nRead == -1)
                return receivedBytes;
            buffer.write(receivedBytes, 0, nRead);
            while (length > allRead) {
                nRead = inputStream.read(receivedBytes, 0, receivedBytes.length);
                allRead += nRead;
                buffer.write(receivedBytes, 0, nRead);
            }

            return buffer.toByteArray();
        }
    }

    /**
     * An abstract method used in handling request (receiveAndSend()).
     *
     * @param request Request for handling.
     * @return Response for request.
     */
    protected abstract Response createResponse(Request request);

    @Override
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}

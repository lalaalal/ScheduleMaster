package com.schedulemaster.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ServerTest {
    @Test
    public void testServer() throws IOException {
        LectureHandler lectureHandler = new LectureHandler("lectures");
        if (lectureHandler.getLectures().isEmpty())
            lectureHandler.appendFromCSV("/Users/lalaalal/Downloads/lectures.csv");

        UserHandler userHandler = new UserHandler("users");
        try (Server server = new Server(lectureHandler, userHandler)) {
            server.run();
        }
    }
}

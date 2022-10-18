package com.schedulemaster.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ServerTest {
    @Test
    public void testServer() throws IOException {
        LectureHandler lectureHandler = new LectureHandler("/Users/lalaalal/Downloads/lectures");
        lectureHandler.appendFromCSV("/Users/lalaalal/Downloads/lectures.csv");

        UserHandler userHandler = new UserHandler("/Users/lalaalal/Downloads/users");
        try (Server server = new Server(lectureHandler, userHandler)) {
            server.run();
        }
    }
}

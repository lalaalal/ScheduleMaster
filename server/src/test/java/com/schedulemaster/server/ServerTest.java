package com.schedulemaster.server;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
@Disabled
public class ServerTest {
    @Test
    public void testServer() throws IOException {
        LectureHandler lectureHandler = new LectureHandler("lectures");
        if (lectureHandler.getLectures().isEmpty())
            lectureHandler.appendFromCSV(System.getProperty("CSV_PATH"));

        UserHandler userHandler = new UserHandler("users");
        try (Server server = new Server(lectureHandler, userHandler)) {
            server.run();
        }
    }
}

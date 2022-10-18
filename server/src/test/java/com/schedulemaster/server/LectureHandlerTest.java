package com.schedulemaster.server;

import com.schedulemaster.model.Lecture;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LectureHandlerTest {
    @Test
    public void testLectureHandler() throws IOException {
        String csvPath = "/Users/lalaalal/Downloads/lectures.csv";

        LectureHandler lectureHandler = new LectureHandler("/Users/lalaalal/Downloads/lectures");
        lectureHandler.appendFromCSV(csvPath);
        for (Lecture lecture : lectureHandler.getLectures()) {
            System.out.println(lecture);
        }
    }
}

package com.schedulemaster.server;

import com.schedulemaster.model.Lecture;
import org.junit.jupiter.api.Test;

public class LectureHandlerTest {
    @Test
    public void testLectureHandler() {
        String path = "/Users/lalaalal/Downloads/lectures.csv";

        LectureHandler lectureHandler = new LectureHandler(path);
        for (Lecture lecture : lectureHandler.getLectures()) {
            System.out.println(lecture);
        }
    }
}

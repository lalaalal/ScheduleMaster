package com.schedulemaster.app;

import com.schedulemaster.app.controller.LectureController;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LectureControllerTest {
    @Test
    public void testLectureSearch() throws IOException {
        try (Client client = new Client()) {
            LectureController lectureController = new LectureController(client);
            System.out.println("Data size : " + lectureController.getLectureBook().getLectures().getLength());

            long start = System.currentTimeMillis();
            lectureController.findByAttributeName(LectureController.AttributeName.Professor, "박현민");
            long end = System.currentTimeMillis();
            System.out.println((end - start) + " ms");
            start = System.currentTimeMillis();
            lectureController.findWithComparator((lecture) -> lecture.professor.equals("박현민"));
            end = System.currentTimeMillis();
            System.out.println((end - start) + " ms");
        }
    }
}

package com.schedulemaster.app;

import com.schedulemaster.app.controller.LectureController;
import com.schedulemaster.app.controller.MagicController;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureBook;
import com.schedulemaster.model.LectureTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Disabled
public class MagicControllerTest {
    @Test
    public void testMagic() throws IOException {
        try (Client client = new Client()) {
            LectureController lectureController = new LectureController(client);
            UserController userController = new UserController(client, lectureController.getLectureBook());
            userController.login("test", "test");
            MagicController magicController = new MagicController(userController, lectureController.getLectureBook());

            LinkedList<Lecture> lectures = client.getLectures();

            magicController.addGroup();
            magicController.addLecture(0, lectures.at(10), 1);
            magicController.addLecture(0, lectures.at(11), 3);
            magicController.addLecture(0, lectures.at(12), 2);

            magicController.addGroup();
            magicController.addLecture(1, lectures.at(20), 1);
            magicController.addLecture(1, lectures.at(21), 3);
            magicController.addLecture(1, lectures.at(22), 0);

            magicController.magic();
            for (Schedule schedule : magicController.getSchedules()) {
                System.out.println(schedule);
            }
        }
    }

    @Test
    public void testSuggestion() throws IOException {
        try (Client client = new Client()) {
            LectureController lectureController = new LectureController(client);
            UserController userController = new UserController(client, lectureController.getLectureBook());
            userController.login("test", "test");

            for (Lecture enrolledLecture : userController.getEnrolledLectures()) {
                System.out.println(enrolledLecture);
            }
            System.out.println("====");

            LectureBook lectureBook = new LectureBook(client.getLectures());
            MagicController magicController = new MagicController(userController, lectureBook);
            userController.addUnwantedTime(new LectureTime.TimeSet(4, LectureTime.Time.parseTime("09:00"), LectureTime.Time.parseTime("21:00")));
            userController.addUnwantedTime(new LectureTime.TimeSet(3, LectureTime.Time.parseTime("09:00"), LectureTime.Time.parseTime("10:50")));

            for (Lecture lecture : magicController.suggest(20)) {
                System.out.println(lecture);
            }
        }
    }
}

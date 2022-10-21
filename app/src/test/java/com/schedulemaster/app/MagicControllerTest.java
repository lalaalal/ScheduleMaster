package com.schedulemaster.app;

import com.schedulemaster.app.controller.MagicController;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MagicControllerTest {
    @Test
    public void testMagic() throws IOException {
        try (Client client = new Client()) {
            UserController userController = new UserController(client);
            userController.login("test", "test");
            MagicController magicController = new MagicController(userController);

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
            UserController userController = new UserController(client);
            userController.login("test", "test");
            MagicController magicController = new MagicController(userController);

            for (Lecture lecture : magicController.suggest()) {
                System.out.println(lecture);
            }
        }
    }
}

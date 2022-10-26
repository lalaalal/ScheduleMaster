package com.schedulemaster.app;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.misc.Request;
import com.schedulemaster.model.Lecture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ClientTest {
    @Test
    public void testGetLectures() throws IOException {
        try (Client client = new Client()) {
            LinkedList<Lecture> lectures =  client.getLectures();
            for (Lecture lecture : lectures) {
                System.out.println(lecture);
            }
        }
    }

    @Test
    public void testSignup() throws IOException {
        try (Client client = new Client()) {
            client.signup("test1", "test1");
        }
    }

    @Test
    public void testLogin() throws IOException {
        try (Client client = new Client()) {
            ResponseStatus loginStatus = client.login("test", "test");
            System.out.println(loginStatus.msg());
            Assertions.assertTrue(loginStatus.status());

            loginStatus = client.login("test1", "test");
            System.out.println(loginStatus.msg());
            Assertions.assertFalse(loginStatus.status());

            loginStatus = client.login("test", "test1");
            System.out.println(loginStatus.msg());
            Assertions.assertFalse(loginStatus.status());
        }
    }

    @Test
    public void testEnroll() throws IOException {
        try (Client client = new Client()) {
            LinkedList<Lecture> lectures =  client.getLectures();
            client.login("test", "test");
            Lecture lecture = lectures.at(10);
            boolean result = client.lectureCommand(Request.ENROLL, lecture).status();
            System.out.println(result);
        }
    }

    @Test
    public void testMultiUser() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try (Client client = new Client()) {
                LinkedList<Lecture> lectures =  client.getLectures();
                client.login("test", "test");
                Lecture lecture = lectures.at(13);
                boolean result = client.lectureCommand(Request.ENROLL, lecture).status();
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try (Client client = new Client()) {
                LinkedList<Lecture> lectures =  client.getLectures();
                client.login("test", "test");
                Lecture lecture = lectures.at(13);
                boolean result = client.lectureCommand(Request.ENROLL, lecture).status();
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}

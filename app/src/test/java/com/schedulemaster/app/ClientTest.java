package com.schedulemaster.app;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ClientTest {
    @Test
    public void testClient() throws IOException {
        try (Client client = new Client()) {
            LinkedList<Lecture> lectures =  client.getLectures();
            for (Lecture lecture : lectures) {
                System.out.println(lecture);
            }
            client.bye();
        }
    }
}

package com.schedulemaster.app;

import com.schedulemaster.model.LectureRating;
import com.schedulemaster.model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class RatingTest {
    @Test
    public void testAddRating() throws IOException {
        try (Client client = new Client("localhost")) {
            client.login("test", "test");
            User user = client.getUserData();
            client.addRating(new LectureRating.Rating("0001", user, 5, "Hello"));
        }
    }

    @Test
    public void testGetRating() throws IOException {
        try (Client client = new Client("localhost")) {

            LectureRating lectureRating = client.getRating("0001");
            for (LectureRating.Rating rating : lectureRating.getRatings())
                System.out.println(rating);
        }
    }
}

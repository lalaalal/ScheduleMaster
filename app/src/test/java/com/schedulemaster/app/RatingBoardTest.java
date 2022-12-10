package com.schedulemaster.app;

import com.schedulemaster.app.view.RatingBoardDialog;
import com.schedulemaster.model.LectureRating;

import java.io.IOException;

public class RatingBoardTest {
    public static void main(String[] args) throws IOException {
        App.setSystemProperties();
        try (Client client = new Client("localhost")) {

            LectureRating lectureRating = client.getRating("0001");
            for (LectureRating.Rating rating : lectureRating.getRatings())
                System.out.println(rating);

            RatingBoardDialog dialog = new RatingBoardDialog(null, "0001");
            dialog.setLectureRating(lectureRating);
            dialog.setVisible(true);
        }
    }
}

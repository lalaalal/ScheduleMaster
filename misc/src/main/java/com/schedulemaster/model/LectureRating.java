package com.schedulemaster.model;

import com.schedulemaster.misc.LinkedList;

import java.io.Serializable;

public class LectureRating implements Serializable {
    public static final long serialVersionUID = 1L;
    private final Lecture lecture;
    private final LinkedList<Rating> ratings = new LinkedList<>();

    public LectureRating(Lecture lecture) {
        this.lecture = lecture;
    }

    public void addRating(User user, int rating, String comment) {
        ratings.push(new Rating(lecture.lectureNum, user, rating, comment));
    }

    public String getLectureName() {
        return lecture.getName();
    }

    public double getAverageRating() {
        double sum = 0;
        for (Rating rating : ratings)
            sum += rating.rating;

        return sum / ratings.getLength();
    }

    public Rating[] getRatings() {
        return ratings.toArray(new Rating[0]);
    }

    public record Rating(String lectureNum, User user, int rating, String comment) implements Serializable {
        @Override
        public String toString() {
            return user.id + " [ " + rating + " ] : " + comment;
        }
    }
}

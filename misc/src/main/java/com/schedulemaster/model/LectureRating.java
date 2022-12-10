package com.schedulemaster.model;

import com.schedulemaster.misc.LinkedList;

import java.io.Serializable;

public class LectureRating implements Serializable {
    public static final long serialVersionUID = 2L;
    private final Lecture lecture;
    private final LinkedList<Rating> ratings = new LinkedList<>();

    public LectureRating(Lecture lecture) {
        this.lecture = lecture;
    }

    public boolean addRating(User user, int rate, String comment) {
        for (Rating rating : ratings) {
            if (rating.user.equals(user))
                return false;
        }

        ratings.push(new Rating(lecture.lectureNum, user, rate, comment));
        return true;
    }

    public String getLectureName() {
        return lecture.getName();
    }

    public double getAverageRating() {
        double sum = 0;
        for (Rating rating : ratings)
            sum += rating.rate;

        return sum / ratings.getLength();
    }

    public void removeRating(User user) {
        Rating target = null;
        for (Rating rating : ratings) {
            if (rating.user.equals(user))
                target = rating;
        }
        if (target != null)
            ratings.remove(target);
    }

    public Rating[] getRatings() {
        return ratings.toArray(new Rating[0]);
    }

    public record Rating(String lectureNum, User user, int rate, String comment) implements Serializable {
        @Override
        public String toString() {
            return user.id + " [ " + rate + " ] : " + comment;
        }
    }
}

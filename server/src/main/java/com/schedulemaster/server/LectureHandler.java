package com.schedulemaster.server;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;

import java.io.IOException;

public class LectureHandler {
    private final LinkedList<Lecture> lectures = new LinkedList<>();

    public LectureHandler(String lectureDataPath) {
        try (CSVReader csvReader = new CSVReader(lectureDataPath)) {
            String[] tuple = csvReader.read();
            while ((tuple = csvReader.read()) != null) {
                Lecture lecture = Lecture.createLecture(tuple);
                Lecture sameLecture = findLecture(lecture.lectureNum);
                if (sameLecture == null) {
                    lectures.push(lecture);
                } else {
                    LinkedList<LectureTime.TimeSet> timeSets = lecture.time.getTimeSets();
                    sameLecture.time.addTimeSets(timeSets);
                }
            }

        } catch (IOException e) {
            System.out.println("Something went wrong while reading csv from " + lectureDataPath);
        }
    }

    public Lecture findLecture(String lectureNumber) {
        for (Lecture lecture : lectures) {
            if (lecture.lectureNum.equals(lectureNumber)) {
                return lecture;
            }
        }

        return null;
    }

    public LinkedList<Lecture> getLectures() {
        return lectures;
    }
}

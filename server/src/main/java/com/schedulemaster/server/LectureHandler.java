package com.schedulemaster.server;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;

import java.io.IOException;

public class LectureHandler {
    private final Hash<String, Lecture> lectures = new Hash<>();

    public LectureHandler(String lectureDataPath) {
        try (CSVReader csvReader = new CSVReader(lectureDataPath)) {
            String[] tuple = csvReader.read();
            while ((tuple = csvReader.read()) != null) {
                Lecture lecture = Lecture.createLecture(tuple);
                addLecture(lecture);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while reading csv from " + lectureDataPath);
        }
    }

    public Lecture findLecture(String lectureNumber) {
        return lectures.get(lectureNumber);
    }

    public void addLecture(Lecture lecture) {
        Lecture sameLecture = findLecture(lecture.lectureNum);
        if (sameLecture == null) {
            lectures.put(lecture.lectureNum, lecture);
        } else {
            LinkedList<LectureTime.TimeSet> timeSets = lecture.time.getTimeSets();
            sameLecture.time.addTimeSets(timeSets);
        }
    }

    public LinkedList<Lecture> getLectures() {
        LinkedList<Lecture> list = new LinkedList<>();
        list.addAll(lectures);
        return list;
    }
}

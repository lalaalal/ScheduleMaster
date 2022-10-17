package com.schedulemaster.server;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;

import java.io.IOException;

public class LectureHandler {
    private final Hash<String, Lecture> lectureHash = new Hash<>();
    private final LinkedList<Lecture> lectures = new LinkedList<>();

    public LectureHandler(String lectureDataPath) {
        try (CSVReader csvReader = new CSVReader(lectureDataPath)) {
            String[] tuple = csvReader.read();
            while ((tuple = csvReader.read()) != null) {
                Lecture lecture = Lecture.createLecture(tuple);
                addLecture(lecture);
            }
            lectures.addAll(lectureHash);
        } catch (IOException e) {
            System.out.println("Something went wrong while reading csv from " + lectureDataPath);
        }
    }

    public Lecture findLecture(String lectureNumber) {
        return lectureHash.get(lectureNumber);
    }

    public void addLecture(Lecture lecture) {
        Lecture sameLecture = findLecture(lecture.lectureNum);
        if (sameLecture == null) {
            lectureHash.put(lecture.lectureNum, lecture);
        } else {
            LinkedList<LectureTime.TimeSet> timeSets = lecture.time.getTimeSets();
            sameLecture.time.addTimeSets(timeSets);
        }
    }

    public LinkedList<Lecture> getLectureHash() {
        return lectures;
    }
}

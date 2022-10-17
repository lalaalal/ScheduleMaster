package com.schedulemaster.server;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;
import com.schedulemaster.model.User;

import java.io.*;

public class LectureHandler {
    private Hash<String, Lecture> lectures;
    private final String lectureDataPath;

    @SuppressWarnings("unchecked")
    public LectureHandler(String lectureDataPath) throws IOException {
        this.lectureDataPath = lectureDataPath;
        try (FileInputStream fis = new FileInputStream(lectureDataPath)) {
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                Object object = ois.readObject();
                lectures = (Hash<String, Lecture>) object;
            }
        } catch (FileNotFoundException | ClassNotFoundException e) {
            lectures = new Hash<>();
        }
    }

    public void initFromCSV(String csvPath) {
        try (CSVReader csvReader = new CSVReader(csvPath)) {
            String[] tuple = csvReader.read();
            while ((tuple = csvReader.read()) != null) {
                Lecture lecture = Lecture.createLecture(tuple);
                addLecture(lecture);
            }
            save();
        } catch (IOException e) {
            System.out.println("Something went wrong while reading csv from " + csvPath);
        }
    }

    public Lecture findLecture(String lectureNumber) {
        return lectures.get(lectureNumber);
    }

    public synchronized void addLecture(Lecture lecture) {
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

    public synchronized boolean enrollLecture(String lectureNum, User user) {
        Lecture lecture = lectures.get(lectureNum);
        if (user.enrolledLectures.has(lecture))
            return false;

        if (lecture.enrolled < lecture.max) {
            lecture.enrolled += 1;
            user.enrollLecture(lecture);
            save();
            return true;
        }
        return false;
    }

    public synchronized boolean cancelLecture(String lectureNum, User user) {
        Lecture lecture = lectures.get(lectureNum);
        if (!user.enrolledLectures.has(lecture))
            return false;

        lecture.enrolled -= 1;
        user.cancelLecture(lecture);
        save();
        return true;
    }

    public synchronized void save() {
        try (FileOutputStream fos = new FileOutputStream(lectureDataPath)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(lectures);
            }
        } catch (IOException e) {
            System.out.println("Failed to save Lectures");
        }
    }
}

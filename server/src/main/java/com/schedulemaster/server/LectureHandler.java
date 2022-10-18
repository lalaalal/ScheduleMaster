package com.schedulemaster.server;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.misc.Request;
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

    public void appendFromCSV(String csvPath) {
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

    public boolean doLectureCommand(String command, Lecture lecture, User user) {
        return switch (command) {
            case Request.ENROLL -> enrollLecture(lecture, user);
            case Request.SELECT -> selectLecture(lecture, user);
            case Request.CANCEL -> cancelLecture(lecture, user);
            case Request.UNSELECT -> unselectLecture(lecture, user);
            default -> false;
        };
    }

    private synchronized boolean addLectureTo(LinkedList<Lecture> list, Lecture lecture) {
        if (list.has(lecture))
            return false;

        if (lecture.enrolled < lecture.max) {
            lecture.enrolled += 1;
            list.push(lecture);
            save();
            return true;
        }
        return false;
    }

    private synchronized boolean removeLectureFrom(LinkedList<Lecture> list, Lecture lecture) {
        if (!list.has(lecture))
            return false;

        lecture.enrolled -= 1;
        list.remove(lecture);
        save();
        return true;
    }

    public synchronized boolean enrollLecture(Lecture lecture, User user) {
        return addLectureTo(user.enrolledLectures, lecture);
    }

    public synchronized boolean selectLecture(Lecture lecture, User user) {
        return addLectureTo(user.selectedLectures, lecture);
    }

    public synchronized boolean cancelLecture(Lecture lecture, User user) {
        return removeLectureFrom(user.enrolledLectures, lecture);
    }

    public synchronized boolean unselectLecture(Lecture lecture, User user) {
        return removeLectureFrom(user.selectedLectures, lecture);
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

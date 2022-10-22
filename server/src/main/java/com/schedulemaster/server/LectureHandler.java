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

    private final Logger logger = Logger.getInstance();

    @SuppressWarnings("unchecked")
    public LectureHandler(String lectureDataPath) {
        this.lectureDataPath = lectureDataPath;
        logger.log("Reading lecture data from \"" + lectureDataPath + "\"", Logger.INFO);
        try (FileInputStream fis = new FileInputStream(lectureDataPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object object = ois.readObject();
            lectures = (Hash<String, Lecture>) object;
        } catch (FileNotFoundException e) {
            lectures = new Hash<>();
            logger.log("No such file : " + lectureDataPath, Logger.ERROR);
        } catch (ClassNotFoundException e) {
            lectures = new Hash<>();
            logger.log("Class not found while reading data from \"" + lectureDataPath + "\"", Logger.ERROR);
        } catch (IOException e) {
            lectures = new Hash<>();
            logger.log("Something went wrong while load lectures from \"" + lectures + "\"", Logger.ERROR);
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
        } catch (FileNotFoundException e) {
            logger.log("No such file : " + csvPath, Logger.ERROR);
        } catch (IOException e) {
            logger.log("\"Something went wrong while reading csv from \"" + csvPath + "\"", Logger.ERROR);
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
        logger.log("Add Lecture (" + lecture + ")", Logger.INFO);
    }

    public LinkedList<Lecture> getLectures() {
        LinkedList<Lecture> list = new LinkedList<>();
        list.addAll(lectures);
        logger.log("Get all Lectures", Logger.INFO);
        return list;
    }

    public boolean doLectureCommand(String command, String lectureNum, User user) {
        return switch (command) {
            case Request.ENROLL -> enrollLecture(lectureNum, user);
            case Request.SELECT -> selectLecture(lectureNum, user);
            case Request.CANCEL -> cancelLecture(lectureNum, user);
            case Request.UNSELECT -> unselectLecture(lectureNum, user);
            default -> false;
        };
    }

    private synchronized boolean addLectureTo(LinkedList<Lecture> list, String lectureNum) {
        Lecture lecture = lectures.get(lectureNum);
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

    private synchronized boolean removeLectureFrom(LinkedList<Lecture> list, String lectureNum) {
        Lecture lecture = lectures.get(lectureNum);
        if (!list.has(lecture))
            return false;

        lecture.enrolled -= 1;
        list.remove(lecture);
        save();
        return true;
    }

    public synchronized boolean enrollLecture(String lectureNum, User user) {
        logger.log(user.id + " enroll " + lectureNum, Logger.INFO);
        return addLectureTo(user.enrolledLectures, lectureNum);
    }

    public synchronized boolean selectLecture(String lectureNum, User user) {
        logger.log(user.id + " select " + lectureNum, Logger.INFO);
        return addLectureTo(user.selectedLectures, lectureNum);
    }

    public synchronized boolean cancelLecture(String lectureNum, User user) {
        logger.log(user.id + " cancel " + lectureNum, Logger.INFO);
        return removeLectureFrom(user.enrolledLectures, lectureNum);
    }

    public synchronized boolean unselectLecture(String lectureNum, User user) {
        logger.log(user.id + " unselect " + lectureNum, Logger.INFO);
        return removeLectureFrom(user.selectedLectures, lectureNum);
    }

    public synchronized void save() {
        logger.log("Saving lectures", Logger.DEBUG);
        try (FileOutputStream fos = new FileOutputStream(lectureDataPath)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(lectures);
            }
        } catch (IOException e) {
            System.out.println("Failed to save Lectures");
        }
    }
}

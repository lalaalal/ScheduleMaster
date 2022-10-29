package com.schedulemaster.server;

import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.misc.Request;
import com.schedulemaster.misc.Response;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;
import com.schedulemaster.model.User;

import java.io.*;
import java.util.Arrays;

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
            logger.log("No such file : \"" + lectureDataPath + "\"", Logger.ERROR);
        } catch (ClassNotFoundException e) {
            lectures = new Hash<>();
            logger.log("Class not found while reading data from \"" + lectureDataPath + "\"", Logger.ERROR);
        } catch (IOException e) {
            lectures = new Hash<>();
            logger.log("Something went wrong while load lectures from \"" + lectures + "\"", Logger.ERROR);
        }
    }

    public void appendFromCSV(String csvPath) {
        logger.log("Appending lectures from csv : \"" + csvPath + "\"", Logger.INFO);
        try (CSVReader csvReader = new CSVReader(csvPath)) {
            String[] tuple = csvReader.read();
            while ((tuple = csvReader.read()) != null) {
                logger.log("Read : " + Arrays.toString(tuple) + "", Logger.VERBOSE);
                Lecture lecture = Lecture.createLecture(tuple);
                addLecture(lecture);
            }
            save();
        } catch (FileNotFoundException e) {
            logger.log("No such file : \"" + csvPath + "\"", Logger.ERROR);
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
        logger.log("Add Lecture (" + lecture + ")", Logger.DEBUG);
    }

    public LinkedList<Lecture> getLectures() {
        LinkedList<Lecture> list = new LinkedList<>();
        list.addAll(lectures);
        logger.log("Get all Lectures", Logger.INFO);
        return list;
    }

    public String doLectureCommand(String command, String lectureNum, User user) {
        return switch (command) {
            case Request.ENROLL -> enrollLecture(lectureNum, user);
            case Request.SELECT -> selectLecture(lectureNum, user);
            case Request.CANCEL -> cancelLecture(lectureNum, user);
            case Request.UNSELECT -> unselectLecture(lectureNum, user);
            default -> Response.FAILED;
        };
    }

    public synchronized String enrollLecture(String lectureNum, User user) {
        logger.log("\"" + user.id + "\" enroll " + lectureNum, Logger.INFO);
        Lecture enrollLecture = lectures.get(lectureNum);
        if (user.enrolledLectures.has(lectureNum))
            return "already_enrolled";

        for (String enrolledLectureNum : user.enrolledLectures) {
            Lecture enrolledLecture = lectures.get(enrolledLectureNum);
            if (enrolledLecture.time.conflictWith(enrollLecture.time)) {
                logger.log(enrolledLecture.lectureNum + " conflict with " + enrollLecture.lectureNum, Logger.INFO);
                return "conflict";
            }
        }

        if (enrollLecture.enrolled < enrollLecture.max) {
            enrollLecture.enrolled += 1;
            user.enrolledLectures.push(lectureNum);
            save();
            return Response.SUCCEED;
        }
        logger.log("Lecture " +  lectureNum + "is already full", Logger.INFO);
        return "lecture_full";
    }

    public synchronized String selectLecture(String lectureNum, User user) {
        logger.log("\"" + user.id + "\" select " + lectureNum, Logger.INFO);
        if (user.selectedLectures.has(lectureNum))
            return "already_selected";

        user.selectedLectures.push(lectureNum);
        save();
        return Response.SUCCEED;
    }

    public synchronized String cancelLecture(String lectureNum, User user) {
        logger.log("\"" + user.id + "\" cancel " + lectureNum, Logger.INFO);
        Lecture lecture = lectures.get(lectureNum);
        if (!user.enrolledLectures.has(lectureNum))
            return "not_enrolled";

        lecture.enrolled -= 1;
        user.enrolledLectures.remove(lectureNum);
        save();
        return Response.SUCCEED;
    }

    public synchronized String unselectLecture(String lectureNum, User user) {
        logger.log("\"" + user.id + "\" unselect " + lectureNum, Logger.INFO);
        if (!user.selectedLectures.has(lectureNum))
            return "not_selected";

        user.selectedLectures.remove(lectureNum);
        save();
        return Response.SUCCEED;
    }

    public synchronized void save() {
        logger.log("Saving lectures to \"" + lectureDataPath + "\"", Logger.DEBUG);
        try (FileOutputStream fos = new FileOutputStream(lectureDataPath)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(lectures);
            }
        } catch (IOException e) {
            logger.log(e.getMessage(), Logger.ERROR);
        }
    }
}

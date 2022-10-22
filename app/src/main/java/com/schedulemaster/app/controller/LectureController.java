package com.schedulemaster.app.controller;

import com.schedulemaster.app.Client;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureBook;

import java.io.IOException;

public class LectureController {
    public enum AttributeName {
        Professor, Major, Name
    }

    private final LectureBook lectureBook;

    public LectureController(Client client) throws IOException {
        lectureBook = new LectureBook(client.getLectures());

        lectureBook.addIndex(AttributeName.Professor.name(), Lecture::getProfessor);
        lectureBook.addIndex(AttributeName.Major.name(), Lecture::getMajor);
        lectureBook.addIndex(AttributeName.Name.name(), Lecture::getName);
    }

    public LinkedList<Lecture> findByAttributeName(AttributeName attributeName, String value) {
        return lectureBook.findLectures(attributeName.name(), value);
    }

    public LinkedList<Lecture> findWithComparator(Comparator comparator, Object comparingValue) {
        LinkedList<Lecture> result = new LinkedList<>();
        for (Lecture lecture : lectureBook.getLectures()) {
            if (comparator.compare(lecture, comparingValue))
                result.push(lecture);
        }

        return result;
    }

    public LectureBook getLectureBook() {
        return lectureBook;
    }

    public interface Comparator {
        boolean compare(Lecture lecture, Object comparingValue);
    }
}

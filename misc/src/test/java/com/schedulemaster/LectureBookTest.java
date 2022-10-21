package com.schedulemaster;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureBook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LectureBookTest {
    public static LinkedList<Lecture> getSampleList() {
        LinkedList<Lecture> lectures = new LinkedList<>();

        Lecture lecture1 = new Lecture();
        lecture1.professor = "A";
        lecture1.lectureNum = "0000";
        Lecture lecture2 = new Lecture();
        lecture2.professor = "A";
        lecture2.lectureNum = "0001";
        Lecture lecture3 = new Lecture();
        lecture3.professor = "B";
        lecture3.lectureNum = "0002";
        Lecture lecture4 = new Lecture();
        lecture4.professor = "A";
        lecture4.lectureNum = "0003";

        lectures.push(lecture1);
        lectures.push(lecture2);
        lectures.push(lecture3);
        lectures.push(lecture4);

        return lectures;
    }

    @Test
    public void testLectureBook() {
        LinkedList<Lecture> lectures = getSampleList();

        LectureBook lectureBook = new LectureBook(lectures);
        lectureBook.addIndex("professor", Lecture::getProfessor);

        LinkedList<Lecture> findByProfessor = lectureBook.findLectures("professor", "A");
        Assertions.assertEquals(3, findByProfessor.getLength());
    }
}

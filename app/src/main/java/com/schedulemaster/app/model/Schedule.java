package com.schedulemaster.app.model;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;

/** *
 * A Group of Lecture ensures no time conflict
 */
public class Schedule {
    private final LinkedList<Lecture> lectures = new LinkedList<>();

    public void addLecture(Lecture lecture) {
        for (Lecture element : lectures) {
            if (element.time.conflictWith(lecture.time))
                return;
        }

        lectures.push(lecture);
    }

    public void removeLecture(Lecture lecture) {
        lectures.remove(lecture);
    }

    public LinkedList<Lecture> getLectures() {
        return lectures;
    }
}

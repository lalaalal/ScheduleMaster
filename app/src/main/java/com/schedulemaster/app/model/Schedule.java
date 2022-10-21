package com.schedulemaster.app.model;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;

/** *
 * A Group of Lecture ensures no time conflict
 */
public class Schedule {
    private final LinkedList<Lecture> lectures = new LinkedList<>();

    public boolean addLecture(Lecture lecture) {
        for (Lecture element : lectures) {
            if (element.time.conflictWith(lecture.time))
                return false;
        }

        lectures.push(lecture);
        return true;
    }

    public void removeLecture(Lecture lecture) {
        lectures.remove(lecture);
    }

    public LinkedList<Lecture> getLectures() {
        return lectures;
    }

    public Schedule copy() {
        Schedule clone = new Schedule();
        clone.lectures.addAll(this.lectures);

        return clone;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "lectures=" + lectures +
                '}';
    }
}

package com.schedulemaster.app.controller;

import com.schedulemaster.app.model.LectureGroup;
import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.User;

public class MagicController {

    private User user;

    private final LinkedList<Schedule> schedules = new LinkedList<>();
    private final LinkedList<LectureGroup> lectureGroups = new LinkedList<>();

    public MagicController() {

    }

    public void addGroup() {
        lectureGroups.push(new LectureGroup());
    }

    public void addLecture(int groupNumber, Lecture lecture) {
        if (lectureGroups.getLength() <= groupNumber)
            throw new IndexOutOfBoundsException();
        LectureGroup group = lectureGroups.at(groupNumber);
        group.push(lecture);
    }

    public void magic() {

    }

    public Schedule[] getSchedules() {
        return schedules.toArray(new Schedule[0]);
    }
}

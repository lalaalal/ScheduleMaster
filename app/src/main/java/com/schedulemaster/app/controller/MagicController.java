package com.schedulemaster.app.controller;

import com.schedulemaster.app.model.LectureGroup;
import com.schedulemaster.app.model.Priority;
import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;

import java.util.Iterator;

public class MagicController {

    private final UserController userController;

    private final LinkedList<Schedule> schedules = new LinkedList<>();
    private final LinkedList<LectureGroup> lectureGroups = new LinkedList<>();
    private final Hash<Lecture, Integer> priorities = new Hash<>();

    public MagicController(UserController userController) {
        this.userController = userController;
    }

    public void addGroup() {
        lectureGroups.push(new LectureGroup());
    }

    public void addLecture(int groupNumber, Lecture lecture, int priority) {
        if (lectureGroups.getLength() <= groupNumber)
            throw new IndexOutOfBoundsException();
        LectureGroup group = lectureGroups.at(groupNumber);
        priorities.put(lecture, priority);
        group.push(lecture);
    }

    public void changePriority(Lecture lecture, int priority) {
        priorities.set(lecture, priority);
    }

    public void magic() {
        schedules.clear();
        createSchedules(lectureGroups.iterator(), null, new Schedule());
    }

    private void createSchedules(Iterator<LectureGroup> iterator, LectureGroup curr, Schedule schedule) {
        if (curr == null && iterator.hasNext())
            curr = iterator.next();
        if (curr == null)
            return;

        LectureGroup next = null;
        if (iterator.hasNext())
            next = iterator.next();

        Heap<Priority> priorityHeap = curr.createHeap(priorities);
        Schedule clone = schedule.copy();
        if (!priorityHeap.isEmpty()) {
            Lecture lecture = priorityHeap.pop().lecture();

            schedule.addLecture(lecture);
            if (next == null)
                schedules.push(schedule);
            else
                createSchedules(iterator, next, schedule);
        }
        while (!priorityHeap.isEmpty()) {
            Lecture lecture = priorityHeap.pop().lecture();

            Schedule newSchedule = clone.copy();
            newSchedule.addLecture(lecture);
            if (next == null)
                schedules.push(newSchedule);
            else
                createSchedules(iterator, next, newSchedule);
        }
    }

    public Schedule[] getSchedules() {
        return schedules.toArray(new Schedule[0]);
    }
}

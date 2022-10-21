package com.schedulemaster.app.controller;

import com.schedulemaster.app.model.LectureGroup;
import com.schedulemaster.app.model.Priority;
import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.User;

import java.util.Iterator;

public class MagicController {

    private final User user;

    private final LinkedList<Schedule> schedules = new LinkedList<>();
    private final LinkedList<LectureGroup> lectureGroups = new LinkedList<>();
    private final Hash<Lecture, Integer> priorities = new Hash<>();

    public MagicController(User user) {
        this.user = user;
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

        while (!priorityHeap.isEmpty()) {
            Lecture lecture = priorityHeap.pop().lecture();

            Schedule clone = schedule.copy();
            clone.addLecture(lecture);
            if (next == null)
                schedules.push(clone);
            else
                createSchedules(iterator, next, clone);
        }
    }

    public Schedule[] getSchedules() {
        return schedules.toArray(new Schedule[0]);
    }
}

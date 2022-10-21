package com.schedulemaster.app.controller;

import com.schedulemaster.model.LectureGroup;
import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.Priority;

import java.io.IOException;
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

    public void removeLecture(int groupNumber, Lecture lecture) {
        if (lectureGroups.getLength() <= groupNumber)
            throw new IndexOutOfBoundsException();
        LectureGroup group = lectureGroups.at(groupNumber);
        group.remove(lecture);
        priorities.remove(lecture);
    }

    /** *
     * Number of later groups will decrease
     */
    public void removeGroup(int groupNumber) {
        if (lectureGroups.getLength() <= groupNumber)
            throw new IndexOutOfBoundsException();
        LectureGroup group = lectureGroups.at(groupNumber);
        lectureGroups.remove(group);
    }

    public void changePriority(Lecture lecture, int priority) {
        priorities.set(lecture, priority);
    }

    public void magic() throws IOException {
        schedules.clear();
        createSchedules(lectureGroups.iterator(), null, new Schedule());
        userController.setPriorities(priorities);
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

    public LinkedList<Lecture> suggest() {
        LinkedList<Lecture> suggestion = new LinkedList<>();
        Heap<Priority> priorityHeap = userController.getPriorityHeap();
        while (!priorityHeap.isEmpty()) {
            Lecture lecture = priorityHeap.pop().lecture();
            suggestion.push(lecture);
        }

        // TODO: Find lectures not conflict with unwantedTime (from LectureBook)

        return suggestion;
    }
}

package com.schedulemaster.app.controller;

import com.schedulemaster.model.*;
import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class MagicController {

    private final UserController userController;

    private final LinkedList<Schedule> schedules = new LinkedList<>();
    private final LinkedList<LectureGroup> lectureGroups = new LinkedList<>();
    private final Hash<Lecture, Integer> priorities = new Hash<>();
    private final LectureBook lectureBook;

    public MagicController(UserController userController, LectureBook lectureBook) {
        this.userController = userController;
        this.lectureBook = lectureBook;
    }

    public void init() {
        lectureGroups.clear();
        priorities.clear();
        schedules.clear();
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
        userController.savePriorities(priorities);
        userController.saveUnwantedTime();
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
        LectureTime unwantedTime = userController.getUnwantedTime();

        while (!priorityHeap.isEmpty()) {
            Lecture lecture = priorityHeap.pop().lecture();

            Schedule clone = schedule.copy();
            if (!lecture.time.conflictWith(unwantedTime))
                clone.addLecture(lecture);
            if (next == null && clone.getLectures().getLength() > 0 && !schedules.has(clone))
                schedules.push(clone);
            else
                createSchedules(iterator, next, clone);
        }
    }

    public Schedule[] getSchedules() {
        Schedule[] result = schedules.toArray(new Schedule[0]);
        Arrays.sort(result, (o1, o2) -> o2.getLectures().getLength() - o1.getLectures().getLength());
        return result;
    }

    public LinkedList<Lecture> suggest(int maxSuggestion) {
        LectureTime usedTime = getUsedTime();

        LinkedList<Lecture> suggestion = new LinkedList<>();
        Heap<Priority> priorityHeap = userController.getPriorityHeap();
        while (!priorityHeap.isEmpty()) {
            Lecture lecture = priorityHeap.pop().lecture();
            if (!lecture.time.conflictWith(usedTime))
                suggestion.push(lecture);
            if (suggestion.getLength() >= maxSuggestion)
                return suggestion;
        }

        LinkedList<Lecture> lectures = lectureBook.getLectures();
        for (Lecture lecture : lectures) {
            if (suggestion.getLength() >= maxSuggestion)
                return suggestion;
            if (!lecture.time.conflictWith(usedTime) && !suggestion.has(lecture))
                suggestion.push(lecture);
        }

        return suggestion;
    }

    private LectureTime getUsedTime() {
        LinkedList<Lecture> enrolledLectures = userController.getEnrolledLectures();
        LectureTime usedTime = new LectureTime();
        usedTime.addTimeSets(userController.getUnwantedTime().getTimeSets());
        for (Lecture enrolledLecture : enrolledLectures) {
            usedTime.addTimeSets(enrolledLecture.time.getTimeSets());
        }

        return usedTime;
    }
}

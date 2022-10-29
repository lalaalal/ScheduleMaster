package com.schedulemaster.app.controller;

import com.schedulemaster.model.*;
import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;

import java.io.IOException;

public class MagicController {

    private final UserController userController;

    private final LinkedList<Schedule> schedules = new LinkedList<>();
    private final LinkedList<LectureGroup> lectureGroups = new LinkedList<>();
    private final Hash<String, Integer> priorities = new Hash<>();
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
        priorities.put(lecture.lectureNum, priority);
        group.push(lecture.lectureNum);
    }

    public void removeLecture(int groupNumber, Lecture lecture) {
        if (lectureGroups.getLength() <= groupNumber)
            throw new IndexOutOfBoundsException();
        LectureGroup group = lectureGroups.at(groupNumber);
        group.remove(lecture.lectureNum);
        priorities.remove(lecture.lectureNum);
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
        priorities.set(lecture.lectureNum, priority);
    }

    public void magic() throws IOException {
        schedules.clear();
        createSchedules(lectureGroups, 0, new Schedule());
        userController.savePriorities(priorities);
        userController.saveUnwantedTime();
    }

    private void createSchedules(LinkedList<LectureGroup> groups, int index, Schedule schedule) {
        if (index >= groups.getLength())
            return;
        LectureGroup curr = groups.at(index);

        Heap<Priority> priorityHeap = curr.createHeap(priorities);
        LectureTime unwantedTime = userController.getUnwantedTime();

        while (!priorityHeap.isEmpty()) {
            String lectureNum = priorityHeap.pop().lectureNum();
            Lecture lecture = lectureBook.findLecture(lectureNum);

            Schedule clone = schedule.copy();
            if (!lecture.time.conflictWith(unwantedTime))
                clone.addLecture(lecture);
            if (index + 1 == groups.getLength() && clone.getLectures().getLength() == groups.getLength())
                schedules.push(clone);
            else
                createSchedules(groups, index + 1, clone);
        }
    }

    public Schedule[] getSchedules() {
        return schedules.toArray(new Schedule[0]);
    }

    public LinkedList<Lecture> suggest(int maxSuggestion) {
        LectureTime usedTime = getUsedTime();

        LinkedList<Lecture> suggestion = new LinkedList<>();
        Heap<Priority> priorityHeap = userController.getPriorityHeap();
        while (!priorityHeap.isEmpty()) {
            String lectureNum = priorityHeap.pop().lectureNum();
            Lecture lecture = lectureBook.findLecture(lectureNum);

            if (!lecture.time.conflictWith(usedTime) && !hasSameName(suggestion, lecture))
                suggestion.push(lecture);
            if (suggestion.getLength() >= maxSuggestion)
                return suggestion;
        }

        LinkedList<Lecture> lectures = lectureBook.getLectures();
        for (Lecture lecture : lectures) {
            if (suggestion.getLength() >= maxSuggestion)
                return suggestion;
            if (!lecture.time.conflictWith(usedTime) && !hasSameName(suggestion, lecture))
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

    private boolean hasSameName(LinkedList<Lecture> lectures, Lecture lecture) {
        for (Lecture compare : lectures) {
            if (compare.name.equals(lecture.name)) {
                return true;
            }
        }
        return false;
    }
}

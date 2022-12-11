package com.schedulemaster.app.controller;

import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.Heap;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.*;

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

        addLectures(suggestion, findMatchingUrgentLectures(), usedTime, maxSuggestion);

        Heap<Priority> priorityHeap = userController.getPriorityHeap();
        while (!priorityHeap.isEmpty()) {
            if (suggestion.getLength() >= maxSuggestion)
                return suggestion;
            String lectureNum = priorityHeap.pop().lectureNum();
            Lecture lecture = lectureBook.findLecture(lectureNum);

            addLecture(suggestion, lecture, usedTime);
        }

        addLectures(suggestion, userController.getSelectedLectures(), usedTime, maxSuggestion);
        LinkedList<Lecture> majorMatchLectures = lectureBook.findLectures(LectureController.AttributeName.Major.name(), userController.getUserMajor());
        addLectures(suggestion, LectureBook.findWithComparator(majorMatchLectures, lecture -> lecture.grade <= userController.getUserGrade()), usedTime, maxSuggestion);
        addLectures(suggestion, lectureBook.getLectures(), usedTime, maxSuggestion);

        return suggestion;
    }

    private LinkedList<Lecture> findMatchingUrgentLectures() {
        LinkedList<Lecture> urgentLectures = LectureBook.findWithComparator(userController.getSelectedLectures(), lecture -> lecture.max <= lecture.enrolled);
        return LectureBook.findWithComparator(lectureBook.getLectures(), lecture -> {
            for (Lecture urgentLecture : urgentLectures) {
                if (lecture.name.equals(urgentLecture.name)
                        && !lecture.lectureNum.equals(urgentLecture.lectureNum)
                        && !userController.getEnrolledLectures().has(urgentLecture))
                    return true;
            }
            return false;
        });
    }

    private void addLecture(LinkedList<Lecture> suggestion, Lecture lecture, LectureTime usedTime) {
        if (!lecture.time.conflictWith(usedTime) && !hasSameName(suggestion, lecture)
                && !hasSameName(userController.getEnrolledLectures(), lecture)
                && !userController.getSelectedLectures().has(lecture)
                && lecture.enrolled != lecture.max)
            suggestion.push(lecture);
    }

    private void addLectures(LinkedList<Lecture> suggestion, LinkedList<Lecture> lectures, LectureTime usedTime, int maxSuggestion) {
        for (Lecture lecture : lectures) {
            if (suggestion.getLength() >= maxSuggestion)
                return;
            addLecture(suggestion, lecture, usedTime);
        }
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

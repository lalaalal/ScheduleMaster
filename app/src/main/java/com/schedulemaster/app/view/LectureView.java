package com.schedulemaster.app.view;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureGroup;

public abstract class LectureView {
    protected LinkedList<Lecture> lectures;

    public LectureView() {
        lectures = new LinkedList<>();
    }

    public LectureView(LinkedList<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void addLecture(Lecture lecture) {
        lectures.push(lecture);
        updateView();
    }

    public void setLectures(LectureGroup lectures) {
        this.lectures = lectures;
        updateView();
    }

    public abstract void updateView();
}

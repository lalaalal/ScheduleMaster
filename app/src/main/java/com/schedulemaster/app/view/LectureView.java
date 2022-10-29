package com.schedulemaster.app.view;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;

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
    }

    public void setLectures(LinkedList<Lecture> lectures) {
        this.lectures = lectures;
        updateView();
    }

    public void clear() {
        lectures.clear();
    }

    public abstract void updateView();
}

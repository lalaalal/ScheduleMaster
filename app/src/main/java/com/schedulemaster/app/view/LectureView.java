package com.schedulemaster.app.view;

import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;

public abstract class LectureView implements ComponentForm {
    protected LinkedList<Lecture> lectures;

    public LectureView() {
        lectures = new LinkedList<>();
    }

    public LectureView(LinkedList<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void addLecture(Lecture lecture) {
        if (!lectures.has(lecture))
            lectures.push(lecture);
    }

    public void addLectures(LinkedList<Lecture> lectures) {
        for (Lecture lecture : lectures) {
            if (!this.lectures.has(lecture))
                this.lectures.push(lecture);
        }
        updateView();
    }

    public void setLectures(LinkedList<Lecture> lectures) {
        this.lectures = lectures;
        updateView();
    }

    public LinkedList<Lecture> getLectures() {
        return lectures;
    }

    public void clear() {
        lectures.clear();
    }

    public abstract void updateView();
}

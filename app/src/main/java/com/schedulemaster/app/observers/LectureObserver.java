package com.schedulemaster.app.observers;

import com.schedulemaster.app.view.LectureView;
import com.schedulemaster.app.view.MainFrame;
import com.schedulemaster.misc.LinkedList;

public abstract class LectureObserver implements Observer {
    protected final MainFrame frame;
    protected final LinkedList<LectureView> lectureViews = new LinkedList<>();

    public LectureObserver(MainFrame frame) {
        this.frame = frame;
    }

    public void addLectureView(LectureView lectureView) {
        lectureViews.push(lectureView);
    }
}

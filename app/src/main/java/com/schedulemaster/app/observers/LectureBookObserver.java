package com.schedulemaster.app.observers;

import com.schedulemaster.app.controller.LectureController;
import com.schedulemaster.app.view.LectureView;
import com.schedulemaster.app.view.MainFrame;
import com.schedulemaster.model.LectureBook;

public class LectureBookObserver extends LectureObserver {
    public LectureBookObserver(MainFrame frame) {
        super(frame);
    }

    @Override
    public void update() {
        LectureController lectureController = frame.getLectureController();
        LectureBook lectureBook = lectureController.getLectureBook();
        for (LectureView lectureView : lectureViews) {
            lectureView.setLectures(lectureBook.getLectures());
        }
    }
}

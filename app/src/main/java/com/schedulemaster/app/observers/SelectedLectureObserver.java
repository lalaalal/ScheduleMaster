package com.schedulemaster.app.observers;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.view.LectureView;
import com.schedulemaster.app.view.MainFrame;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;

public class SelectedLectureObserver extends LectureObserver {

    public SelectedLectureObserver(MainFrame frame) {
        super(frame);
    }

    @Override
    public void update() {
        UserController userController = frame.getUserController();
        LinkedList<Lecture> selectedLectures = userController.getSelectedLectures();
        for (LectureView lectureView : lectureViews) {
            lectureView.setLectures(selectedLectures);
        }
    }
}

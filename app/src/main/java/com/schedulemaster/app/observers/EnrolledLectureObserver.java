package com.schedulemaster.app.observers;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.view.LectureView;
import com.schedulemaster.app.view.MainFrame;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;

public class EnrolledLectureObserver extends LectureObserver {

    public EnrolledLectureObserver(MainFrame frame) {
        super(frame);
    }

    @Override
    public void update() {
        UserController userController = frame.getUserController();
        LinkedList<Lecture> enrolledLectures = userController.getEnrolledLectures();
        for (LectureView lectureView : lectureViews) {
            lectureView.setLectures(enrolledLectures);
            lectureView.updateView();
        }
    }
}

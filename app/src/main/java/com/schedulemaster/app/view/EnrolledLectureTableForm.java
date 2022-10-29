package com.schedulemaster.app.view;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.model.Lecture;

import javax.swing.*;

public class EnrolledLectureTableForm extends LectureTableForm {
    public EnrolledLectureTableForm(MainFrame frame) {
        super(frame);
    }

    @Override
    protected JButton createButton1(Lecture lecture) {
        return null;
    }

    @Override
    protected JButton createButton2(Lecture lecture) {
        return createButton(lecture, "취소", UserController::cancelLecture);
    }
}

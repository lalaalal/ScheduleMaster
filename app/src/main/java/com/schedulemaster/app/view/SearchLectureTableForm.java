package com.schedulemaster.app.view;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.model.Lecture;

import javax.swing.*;

public class SearchLectureTableForm extends LectureTableForm {
    public SearchLectureTableForm(MainFrame frame) {
        super(frame);
    }

    @Override
    public JButton createButton1(Lecture lecture) {
        return createButton(lecture, "신청", UserController::enrollLecture);
    }

    @Override
    public JButton createButton2(Lecture lecture) {
        return createButton(lecture, "담기", UserController::selectLecture);
    }
}

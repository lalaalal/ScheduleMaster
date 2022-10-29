package com.schedulemaster.app.view;

import com.schedulemaster.app.controller.UserController;

import java.util.ResourceBundle;

public class HomeLectureTableForm extends LectureTableForm {
    public HomeLectureTableForm(MainFrame frame) {
        super(frame);
        String enrollColumnName = ResourceBundle.getBundle(MainFrame.RESOURCE_BUNDLE_NAME).getString("enroll");
        String selectColumnName = ResourceBundle.getBundle(MainFrame.RESOURCE_BUNDLE_NAME).getString("select");
        addButtonColumn(enrollColumnName, UserController::enrollLecture);
        addButtonColumn(selectColumnName, UserController::selectLecture);
    }
}

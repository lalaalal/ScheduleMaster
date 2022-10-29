package com.schedulemaster.app.view;

import com.schedulemaster.app.controller.UserController;

import java.util.ResourceBundle;

public class EnrolledLectureTableForm extends LectureTableForm {
    public EnrolledLectureTableForm(MainFrame frame) {
        super(frame);

        String cancelColumnName = ResourceBundle.getBundle(MainFrame.RESOURCE_BUNDLE_NAME).getString("cancel");
        addButtonColumn(cancelColumnName, UserController::cancelLecture);
    }
}

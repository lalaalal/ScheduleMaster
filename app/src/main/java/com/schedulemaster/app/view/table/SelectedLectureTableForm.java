package com.schedulemaster.app.view.table;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.view.MainFrame;

import java.util.ResourceBundle;

public class SelectedLectureTableForm extends LectureTableForm {

    public SelectedLectureTableForm(MainFrame frame) {
        super(frame);
        String enrollColumnName = ResourceBundle.getBundle(MainFrame.RESOURCE_BUNDLE_NAME).getString("enroll");
        String unselectColumnName = ResourceBundle.getBundle(MainFrame.RESOURCE_BUNDLE_NAME).getString("unselect");
        addButtonColumn(enrollColumnName, UserController::enrollLecture);
        addButtonColumn(unselectColumnName, UserController::unselectLecture);
    }
}

package com.schedulemaster.app.view.table;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.view.MainFrame;

public class SelectedLectureTableForm extends LectureTableForm {

    public SelectedLectureTableForm(MainFrame frame) {
        super(frame);
        String enrollColumnName = "enroll";
        String unselectColumnName = "unselect";
        addButtonColumn(enrollColumnName, UserController::enrollLecture);
        addButtonColumn(unselectColumnName, UserController::unselectLecture);
    }
}

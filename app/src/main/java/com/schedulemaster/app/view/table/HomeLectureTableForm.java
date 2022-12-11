package com.schedulemaster.app.view.table;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.view.MainFrame;

public class HomeLectureTableForm extends LectureTableForm {
    public HomeLectureTableForm(MainFrame frame) {
        super(frame);
        String enrollColumnName = "enroll";
        String selectColumnName = "select";
        addButtonColumn(enrollColumnName, UserController::enrollLecture);
        addButtonColumn(selectColumnName, UserController::selectLecture);
    }

    @Override
    public void updateView() {

        super.updateView();
    }
}

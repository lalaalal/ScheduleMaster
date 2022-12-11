package com.schedulemaster.app.view.table;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.view.MainFrame;

public class EnrolledLectureTableForm extends LectureTableForm {
    public EnrolledLectureTableForm(MainFrame frame) {
        super(frame);

        String cancelColumnName = "cancel";
        addButtonColumn(cancelColumnName, UserController::cancelLecture);
    }
}

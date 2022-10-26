package com.schedulemaster.app.view;

import com.schedulemaster.app.controller.LectureController;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureBook;

import javax.swing.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class SearchLectureTableForm extends LectureTableForm {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("string");

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

    @Override
    public void update() {
        try {
            LectureController lectureController = frame.getLectureController();
            lectureController.refresh();
            LectureBook lectureBook = lectureController.getLectureBook();
            setLectures(lectureBook.getLectures());
            updateView();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(frame, e.getLocalizedMessage(), resourceBundle.getString("error"), JOptionPane.DEFAULT_OPTION);
        }
    }
}

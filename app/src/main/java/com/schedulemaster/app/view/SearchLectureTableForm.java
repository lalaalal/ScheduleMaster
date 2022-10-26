package com.schedulemaster.app.view;

import com.schedulemaster.app.controller.LectureController;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureBook;

import javax.swing.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class SearchLectureTableForm extends LectureTableForm {
    private final MainFrame frame;
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("string");

    public SearchLectureTableForm(MainFrame frame) {
        this.frame = frame;
    }

    @Override
    public JButton createButton1(Lecture lecture) {
        JButton enrollButton = new JButton("신청");
        enrollButton.addActionListener(event -> {
            try {
                UserController userController = frame.getUserController();
                String message = "failed";
                if (userController.enrollLecture(lecture))
                    message = "succeed";
                JOptionPane.showConfirmDialog(frame, resourceBundle.getString(message), resourceBundle.getString("info"), JOptionPane.DEFAULT_OPTION);
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(frame, e.getLocalizedMessage(), resourceBundle.getString("error"), JOptionPane.DEFAULT_OPTION);
            }
        });

        return enrollButton;
    }

    @Override
    public JButton createButton2(Lecture lecture) {
        return new JButton("담기");
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

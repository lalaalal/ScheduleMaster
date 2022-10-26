package com.schedulemaster.app.view;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.model.Lecture;

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
                userController.enrollLecture(lecture);
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
}

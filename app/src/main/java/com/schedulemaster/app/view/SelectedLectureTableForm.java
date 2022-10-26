package com.schedulemaster.app.view;

import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.model.Lecture;

import javax.swing.*;
import java.io.IOException;

public class SelectedLectureTableForm extends LectureTableForm {

    public SelectedLectureTableForm(MainFrame frame) {
        super(frame);
    }

    @Override
    public JButton createButton1(Lecture lecture) {
        return new JButton("취소");
    }

    @Override
    public JButton createButton2(Lecture lecture) {
        return new JButton("빼기");
    }

    @Override
    public void update() {
        try {
            UserController userController = frame.getUserController();
            userController.refresh();
            setLectures(userController.getSelectedLectures());
            updateView();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(frame, e.getLocalizedMessage(), resourceBundle.getString("error"), JOptionPane.DEFAULT_OPTION);
        }
    }
}

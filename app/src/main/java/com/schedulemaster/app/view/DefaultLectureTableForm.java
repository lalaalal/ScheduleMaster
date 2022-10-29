package com.schedulemaster.app.view;

import com.schedulemaster.model.Lecture;

import javax.swing.*;

public class DefaultLectureTableForm extends LectureTableForm {
    public DefaultLectureTableForm(MainFrame frame) {
        super(frame);
    }

    @Override
    protected JButton createButton1(Lecture lecture) {
        return null;
    }

    @Override
    protected JButton createButton2(Lecture lecture) {
        return null;
    }
}

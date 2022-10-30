package com.schedulemaster.app;

import com.schedulemaster.app.view.TimeTableForm;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;
import mdlaf.MaterialLookAndFeel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

@Disabled
public class TimeTableFormTest {
    @Test
    public void testCalcClassTime(){
        TimeTableForm timeTableForm = new TimeTableForm();
        LectureTime.Time start = LectureTime.Time.parseTime("13:00");
        LectureTime.Time end = LectureTime.Time.parseTime("14:50");
        LectureTime.TimeSet timeSet = new LectureTime.TimeSet(0, start, end);
        int[] classTimes = timeTableForm.calcClassTimes(timeSet);
        for (int classTime : classTimes) {
            System.out.println(classTime);
        }
    }

    @Test
    public void testUpdateView() throws InterruptedException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new MaterialLookAndFeel());
        UIManager.put("Table.gridColor", new ColorUIResource(Color.gray));
        JFrame frame = new JFrame();
        frame.setSize(500, 400);
        TimeTableForm timeTableForm = new TimeTableForm();
        frame.setContentPane(timeTableForm.getPanel());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Lecture lecture = new Lecture();
        LectureTime.Time start = LectureTime.Time.parseTime("13:00");
        LectureTime.Time end = LectureTime.Time.parseTime("14:50");
        LectureTime.TimeSet timeSet = new LectureTime.TimeSet(0, start, end);
        lecture.time = new LectureTime();
        lecture.time.addTimeSet(timeSet);

        Lecture lecture2 = new Lecture();
        start = LectureTime.Time.parseTime("13:00");
        end = LectureTime.Time.parseTime("14:50");
        timeSet = new LectureTime.TimeSet(2, start, end);
        lecture2.time = new LectureTime();
        lecture2.time.addTimeSet(timeSet);

        timeTableForm.addLecture(lecture);
        timeTableForm.addLecture(lecture2);
        timeTableForm.updateView();

        Thread.sleep(15000);
    }
}

package com.schedulemaster.app.view;

import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectableTimeTable extends TimeTableForm {
    private class SelectorMouseAdapter extends MouseAdapter {
        private final JTable timeTable;
        private int currentRow = 0;
        private int currentColumn = 0;

        public SelectorMouseAdapter(JTable timeTable) {
            this.timeTable = timeTable;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            currentRow = timeTable.rowAtPoint(e.getPoint());
            currentColumn = timeTable.columnAtPoint(e.getPoint());
            click(currentRow, currentColumn);
            updateView();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int row = timeTable.rowAtPoint(e.getPoint());
            int col = timeTable.columnAtPoint(e.getPoint());

            if (row != currentRow || col != currentColumn) {
                click(row, col);
                currentRow = row;
                currentColumn = col;
                updateView();
            }
        }
    }

    private final LectureTime selectedTime = new LectureTime();
    private final Lecture lecture;

    public SelectableTimeTable() {
        JTable timeTable = getTimeTable();
        MouseAdapter mouseAdapter = new SelectorMouseAdapter(timeTable);
        timeTable.addMouseListener(mouseAdapter);
        timeTable.addMouseMotionListener(mouseAdapter);

        lecture = new Lecture();
        lecture.name = "";
        lecture.lectureNum = "";
        lecture.time = selectedTime;
        addLecture(lecture);
    }

    public void click(int row, int column) {
        int dayOfWeek = column - 1;
        LectureTime.Time start = CLASS_TIME[row];
        LectureTime.Time end = CLASS_TIME[row + 1];
        LectureTime.TimeSet timeSet = new LectureTime.TimeSet(dayOfWeek, start, end);

        if (selectedTime.hasTimeSet(timeSet))
            selectedTime.removeTimeSet(timeSet);
        else
            selectedTime.addTimeSet(timeSet);

        clear();
        addLecture(lecture);
    }

    public LectureTime getSelectedTime() {
        return selectedTime;
    }
}

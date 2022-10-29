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
        private boolean selectMode = true;

        public SelectorMouseAdapter(JTable timeTable) {
            this.timeTable = timeTable;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            currentRow = timeTable.rowAtPoint(e.getPoint());
            currentColumn = timeTable.columnAtPoint(e.getPoint());

            selectMode = !isSelected(currentRow, currentColumn);
            toggle(currentRow, currentColumn);
            updateView();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int row = timeTable.rowAtPoint(e.getPoint());
            int col = timeTable.columnAtPoint(e.getPoint());
            if (row < 0 || col < 0 || row >= timeTable.getRowCount() || col >= timeTable.getColumnCount())
                return;

            if (row != currentRow || col != currentColumn) {
                changeStatus(row, col, selectMode);
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

    public LectureTime.TimeSet getTimeSet(int row, int column) {
        int dayOfWeek = column - 1;
        if (dayOfWeek < 0)
            return null;
        LectureTime.Time start = CLASS_TIME[row];
        LectureTime.Time end = CLASS_TIME[row + 1];

        return new LectureTime.TimeSet(dayOfWeek, start, end);
    }

    public boolean isSelected(int row, int column) {
        return selectedTime.hasTimeSet(getTimeSet(row, column));
    }

    public void toggle(int row, int column) {
        LectureTime.TimeSet timeSet = getTimeSet(row, column);
        if (timeSet == null)
            return;

        if (selectedTime.hasTimeSet(timeSet))
            selectedTime.removeTimeSet(timeSet);
        else
            selectedTime.addTimeSet(timeSet);

        clear();
        addLecture(lecture);
    }

    public void changeStatus(int row, int column, boolean selectMode) {
        if (selectMode)
            select(row, column);
        else
            unselect(row, column);
    }

    public void select(int row, int column) {
        LectureTime.TimeSet timeSet = getTimeSet(row, column);
        if (timeSet == null)
            return;

        if (!selectedTime.hasTimeSet(timeSet))
            selectedTime.addTimeSet(timeSet);

        clear();
        addLecture(lecture);
    }

    public void unselect(int row, int column) {
        LectureTime.TimeSet timeSet = getTimeSet(row, column);
        if (timeSet == null)
            return;

        if (selectedTime.hasTimeSet(timeSet))
            selectedTime.removeTimeSet(timeSet);

        clear();
        addLecture(lecture);
    }

    public void setSelectedTime(LectureTime lectureTime) {
        selectedTime.clear();
        selectedTime.addTimeSets(lectureTime.getTimeSets());
        updateView();
    }

    public LectureTime getSelectedTime() {
        return selectedTime;
    }
}

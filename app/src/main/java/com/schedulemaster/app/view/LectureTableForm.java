package com.schedulemaster.app.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.schedulemaster.app.Observer;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class LectureTableForm extends LectureView implements Observer {
    private static class JButtonRenderer implements TableCellRenderer {
        private final TableCellRenderer defaultRenderer;

        public JButtonRenderer(TableCellRenderer renderer) {
            defaultRenderer = renderer;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Component)
                return (Component) value;
            return defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private static class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
        public WordWrapCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setAlignmentX(CENTER_ALIGNMENT);
            setText(value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height);
            }
            return this;
        }
    }

    private class TableMouseAdapter extends  MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int column = lectureTable.getColumnModel().getColumnIndexAtX(e.getX()); // get the column of the button
            int row = e.getY() / lectureTable.getRowHeight(); //get the row of the button

            /*Checking the row or column is valid or not*/
            if (row < lectureTable.getRowCount() && row >= 0 && column < lectureTable.getColumnCount() && column >= 0) {
                Object value = lectureTable.getValueAt(row, column);
                if (value instanceof JButton) {
                    /*perform a click event*/
                    ((JButton) value).doClick();
                }
            }
        }
    }

    private static final String[] HEADER = {"전공", "강의번호", "강의명", "교수명", "학점", "강의시간", "강의실", "신청자", "최대", "1", "2"};

    private JTable lectureTable;
    private JPanel panel;
    private JScrollPane scrollPane;
    private final DefaultTableModel tableModel = new DefaultTableModel(HEADER, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public LectureTableForm() {
        $$$setupUI$$$();
        lectureTable.getColumn("강의명").setMinWidth(200);
        lectureTable.getColumn("강의시간").setMinWidth(200);
        lectureTable.addMouseListener(new TableMouseAdapter());
    }

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void updateView() {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        for (Lecture lecture : lectures) {
            Object[] rowData = createRowData(lecture);
            tableModel.addRow(rowData);
        }
    }

    private Object[] createRowData(Lecture lecture) {
        Object[] rowData = new Object[HEADER.length];
        rowData[0] = lecture.major;
        rowData[1] = lecture.lectureNum;
        rowData[2] = lecture.name;
        rowData[3] = lecture.professor;
        rowData[4] = String.valueOf(lecture.score);
        rowData[5] = lectureTime(lecture.time);
        rowData[6] = lecture.classRoom;
        rowData[7] = String.valueOf(lecture.enrolled);
        rowData[8] = String.valueOf(lecture.max);
        rowData[9] = createButton1(lecture);
        rowData[10] = createButton2(lecture);

        return rowData;
    }

    public abstract JButton createButton1(Lecture lecture);
    public abstract JButton createButton2(Lecture lecture);

    private String lectureTime(LectureTime time) {
        StringBuilder stringBuilder = new StringBuilder();
        LinkedList<LectureTime.TimeSet> timeSets = time.getTimeSets();
        int i = 0;
        for (LectureTime.TimeSet timeSet : timeSets) {
            stringBuilder.append(timeSet.toString());
            if (i < timeSets.getLength() - 1)
                stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    private void createUIComponents() {
        lectureTable = new JTable(tableModel);
        lectureTable.setDefaultRenderer(String.class, new WordWrapCellRenderer());
        TableCellRenderer tableCellRenderer = lectureTable.getDefaultRenderer(JButton.class);
        JButtonRenderer buttonRenderer = new JButtonRenderer(tableCellRenderer);
        lectureTable.getColumn("1").setCellRenderer(buttonRenderer);
        lectureTable.getColumn("2").setCellRenderer(buttonRenderer);

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scrollPane = new JScrollPane();
        scrollPane.setEnabled(true);
        panel.add(scrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        lectureTable.setEnabled(true);
        scrollPane.setViewportView(lectureTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}

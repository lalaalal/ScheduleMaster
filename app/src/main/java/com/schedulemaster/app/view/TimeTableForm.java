package com.schedulemaster.app.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TimeTableForm extends LectureView {
    private record Position(int row, int column) {
    }

    private final Hash<Position, Color> colors = new Hash<>();

    private JTable timeTable;
    private JPanel panel;
    private JScrollPane scrollPane;

    private static final String[] HEADER = {"", "월", "화", "수", "목", "금"};
    private static final String[][] RAW_DATA = {
            {"1교시", "", "", "", "", ""}, // 9
            {"2교시", "", "", "", "", ""}, // 10
            {"3교시", "", "", "", "", ""}, // 11
            {"4교시", "", "", "", "", ""}, // 12
            {"5교시", "", "", "", "", ""}, // 13
            {"6교시", "", "", "", "", ""}, // 14
            {"7교시", "", "", "", "", ""}, // 15
            {"8교시", "", "", "", "", ""}, // 16
            {"9교시", "", "", "", "", ""}, // 17
            {"10교시", "", "", "", "", ""} // 18
    };

    private static final int FIRST_CLASS_HOUR = 9;
    private static final int LAST_CLASS_HOUR = 19;

    private static final LectureTime.Time[] CLASS_TIME = new LectureTime.Time[10];

    private static final Color[] COLORS = {Color.BLUE, Color.RED, Color.GREEN, Color.LIGHT_GRAY};

    static {
        for (int time = FIRST_CLASS_HOUR, index = 0; time < LAST_CLASS_HOUR; time++, index++) {
            CLASS_TIME[index] = new LectureTime.Time(time, 0);
        }
    }

    public TimeTableForm() {
        $$$setupUI$$$();
        timeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        timeTable.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    @Override
    public void updateView() {
        colors.clear();
        int colorIndex = 0;
        for (Lecture lecture : lectures) {
            setClassTimes(lecture.getTime(), colorIndex);
            colorIndex += 1;
        }
    }

    private void setClassTimes(LectureTime lectureTime, int colorIndex) {
        LinkedList<LectureTime.TimeSet> timeSets = lectureTime.getTimeSets();

        for (LectureTime.TimeSet timeSet : timeSets) {
            int[] rows = calcClassTimes(timeSet);
            for (int row : rows) {
                Position position = new Position(row, timeSet.dayOfWeek() + 1);
                colors.put(position, COLORS[colorIndex % COLORS.length]);
            }

        }

    }

    public int[] calcClassTimes(LectureTime.TimeSet timeSet) {
        int startClassIndex = 0;
        if (timeSet.start().isAfter(CLASS_TIME[9]))
            return new int[0];
        LectureTime.TimeSet compare = new LectureTime.TimeSet(timeSet.dayOfWeek(), CLASS_TIME[startClassIndex], CLASS_TIME[9]);
        while (compare.include(timeSet)) {
            startClassIndex += 1;
            compare = new LectureTime.TimeSet(timeSet.dayOfWeek(), CLASS_TIME[startClassIndex], CLASS_TIME[9]);
        }
        if (startClassIndex > 0)
            startClassIndex -= 1;

        int endClassIndex = 9;
        compare = new LectureTime.TimeSet(timeSet.dayOfWeek(), CLASS_TIME[startClassIndex], CLASS_TIME[endClassIndex]);
        while (endClassIndex > startClassIndex && compare.include(timeSet)) {
            endClassIndex -= 1;
            compare = new LectureTime.TimeSet(timeSet.dayOfWeek(), CLASS_TIME[startClassIndex], CLASS_TIME[endClassIndex]);
        }

        int length = endClassIndex - startClassIndex + 1;
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = startClassIndex + i;
        }

        return result;
    }

    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {
        timeTable = new JTable(RAW_DATA, HEADER);
        timeTable.getTableHeader().setReorderingAllowed(false);
        timeTable.setEnabled(false);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JComponent component = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.LIGHT_GRAY));
                Position position = new Position(row, column);
                Color color = colors.get(position);
                if (color != null)
                    component.setBackground(color);
                else
                    component.setBackground(Color.getColor("383E49"));
                return component;
            }
        };
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        timeTable.setDefaultRenderer(Object.class, renderer);
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
        panel.add(scrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        timeTable.setMinimumSize(new Dimension(30, 30));
        scrollPane.setViewportView(timeTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}

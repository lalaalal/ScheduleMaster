package com.schedulemaster.app.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.schedulemaster.app.ResponseStatus;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureTime;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ResourceBundle;

public class LectureTableForm extends LectureView {
    private static class JButtonRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Component)
                return (Component) value;
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private class TableMouseAdapter extends MouseAdapter {
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

    private static final String[] HEADER = {"전공", "강의번호", "강의명", "교수명", "학점", "강의시간", "강의실", "신청자", "최대"};

    private JTable lectureTable;
    private JPanel panel;
    private JScrollPane scrollPane;
    private final JButtonRenderer buttonRenderer = new JButtonRenderer();

    protected final MainFrame frame;
    protected final ResourceBundle resourceBundle = ResourceBundle.getBundle("string");

    private int buttonColumnCount = 0;

    private final Hash<Integer, UserAction> actions = new Hash<>();

    private final DefaultTableModel tableModel = new DefaultTableModel(HEADER, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public LectureTableForm(MainFrame frame) {
        $$$setupUI$$$();
        lectureTable.getColumn("강의명").setMinWidth(200);
        lectureTable.getColumn("강의시간").setMinWidth(175);
        lectureTable.getColumn("강의번호").setMinWidth(50);
        lectureTable.addMouseListener(new TableMouseAdapter());
        lectureTable.setRowHeight((int) (lectureTable.getRowHeight() * 1.2));
        this.frame = frame;

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(scrollPane.getBorder(), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)));
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void updateView() {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        if (lectures == null)
            return;
        for (Lecture lecture : lectures) {
            Object[] rowData = createRowData(lecture);
            tableModel.addRow(rowData);
        }
    }

    private Object[] createRowData(Lecture lecture) {
        Object[] rowData = new Object[HEADER.length + buttonColumnCount];
        rowData[0] = lecture.major;
        rowData[1] = lecture.lectureNum;
        rowData[2] = lecture.name;
        rowData[3] = lecture.professor;
        rowData[4] = String.valueOf(lecture.score);
        rowData[5] = lectureTime(lecture.time);
        rowData[6] = lecture.classRoom;
        rowData[7] = String.valueOf(lecture.enrolled);
        rowData[8] = String.valueOf(lecture.max);

        for (int i = 0; i < buttonColumnCount; i++) {
            int columnIndex = HEADER.length + i;
            String columnName = tableModel.getColumnName(columnIndex);
            rowData[columnIndex] = createUserControlButton(lecture, columnName, actions.get(columnIndex));
        }

        return rowData;
    }

    public void addButtonColumn(String columnName, UserAction userAction) {
        tableModel.addColumn(columnName);
        for (int i = 0; i < buttonColumnCount + 1; i++) {
            int columnIndex = HEADER.length + i;
            String identifier = tableModel.getColumnName(columnIndex);
            lectureTable.getColumn(identifier).setCellRenderer(buttonRenderer);

            lectureTable.getColumn("강의명").setMinWidth(200);
            lectureTable.getColumn("강의시간").setMinWidth(175);
            lectureTable.getColumn("강의번호").setMinWidth(50);
        }
        actions.put(HEADER.length + buttonColumnCount, userAction);
        buttonColumnCount += 1;
    }

    public JButton createUserControlButton(Lecture lecture, String name, UserAction userAction) {
        JButton enrollButton = new JButton(name);
        enrollButton.addActionListener(event -> {
            try {
                UserController userController = frame.getUserController();
                ResponseStatus status = userAction.action(userController, lecture);
                if (status.status())
                    frame.getLectureController().refresh();
                JOptionPane.showConfirmDialog(frame, resourceBundle.getString(status.msg()), resourceBundle.getString("info"), JOptionPane.DEFAULT_OPTION);
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(frame, e.getLocalizedMessage(), resourceBundle.getString("error"), JOptionPane.DEFAULT_OPTION);
            }
        });

        return enrollButton;
    }

    protected interface UserAction {
        ResponseStatus action(UserController userController, Lecture lecture) throws IOException;
    }

    private String lectureTime(LectureTime time) {
        StringBuilder stringBuilder = new StringBuilder("<html>");
        LinkedList<LectureTime.TimeSet> timeSets = time.getTimeSets();
        int i = 0;
        for (LectureTime.TimeSet timeSet : timeSets) {
            stringBuilder.append(timeSet.toString());
            if (i < timeSets.getLength() - 1)
                stringBuilder.append("<br>");
        }
        return stringBuilder.toString();
    }

    public LinkedList<Lecture> getSelectedLectures() {
        LinkedList<Lecture> selectedLectures = new LinkedList<>();
        for (int selectedRow : lectureTable.getSelectedRows()) {
            selectedLectures.push(lectures.at(selectedRow));
        }

        return selectedLectures;
    }

    private void createUIComponents() {
        lectureTable = new JTable(tableModel);
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
        panel.add(scrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 250), null, 0, false));
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
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

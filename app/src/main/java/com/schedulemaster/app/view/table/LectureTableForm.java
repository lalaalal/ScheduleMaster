package com.schedulemaster.app.view.table;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.schedulemaster.app.ResponseStatus;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.util.ThemeManager;
import com.schedulemaster.app.util.Translator;
import com.schedulemaster.app.view.LectureView;
import com.schedulemaster.app.view.MainFrame;
import com.schedulemaster.app.view.RatingBoardDialog;
import com.schedulemaster.misc.Hash;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;
import com.schedulemaster.model.LectureRating;
import com.schedulemaster.model.LectureTime;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
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

    private static final String[] HEADER = {"major", "grade", "lecture_num", "lecture_name", "professor", "score", "lecture_time", "room", "enrolled_num", "max"};

    private JTable lectureTable;
    private JPanel panel;
    private JScrollPane scrollPane;
    private final JButtonRenderer buttonRenderer = new JButtonRenderer();

    protected final MainFrame frame;
    protected final ResourceBundle resourceBundle = ResourceBundle.getBundle("string");

    private int lastColumnIndex = HEADER.length - 1;
    private final LinkedList<Integer> userActionButtonColumns = new LinkedList<>();
    private final LinkedList<Integer> actionButtonColumns = new LinkedList<>();

    private final Hash<Integer, UserAction> userActions = new Hash<>();
    private final Hash<Integer, Action> actions = new Hash<>();
    private final Hash<Integer, String> columnNames = new Hash<>();

    private final DefaultTableModel tableModel = new DefaultTableModel(HEADER, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public LectureTableForm(MainFrame frame) {
        $$$setupUI$$$();
        setColumnWidth();
        lectureTable.addMouseListener(new TableMouseAdapter());
        lectureTable.setRowHeight((int) (lectureTable.getRowHeight() * 1.2));
        this.frame = frame;

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        lectureTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    try {
                        String lectureNum = getSelectedLectureNum();
                        LectureRating rating = frame.getLectureController().getLectureRating(lectureNum);
                        RatingBoardDialog ratingBoardDialog = new RatingBoardDialog(frame, lectureNum);
                        ratingBoardDialog.setLectureRating(rating);
                        ratingBoardDialog.setVisible(true);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        addThemeChangeListener(() -> {
            Border lineBorder = BorderFactory.createLineBorder(ThemeManager.getInstance().getColor("Table.cellBorder"), 1);
            scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15), lineBorder));
            lectureTable.setRowHeight((int) (lectureTable.getRowHeight() * 1.3));
            scrollPane.getViewport().setBackground(ThemeManager.getDefaultColor("Panel.background"));
        });
        addLocaleChangeListener(() -> {
            JTableHeader tableHeader = lectureTable.getTableHeader();
            TableColumnModel columnModel = tableHeader.getColumnModel();
            for (int i = 0; i < HEADER.length; i++) {
                TableColumn tableColumn = columnModel.getColumn(i);
                tableColumn.setHeaderValue(Translator.getBundleString(HEADER[i]));
            }
            for (int i = HEADER.length; i < columnModel.getColumnCount(); i++) {
                TableColumn tableColumn = columnModel.getColumn(i);
                tableColumn.setHeaderValue(Translator.getBundleString(columnNames.get(i)));
            }
            updateView();
        });
    }

    private String getSelectedLectureNum() {
        int row = lectureTable.getSelectedRow();
        int modelIndex = lectureTable.getColumn(Translator.getBundleString("lecture_num")).getModelIndex();
        int column = lectureTable.convertColumnIndexToView(modelIndex);
        return lectureTable.getValueAt(row, column).toString();
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
        Object[] rowData = new Object[HEADER.length + lastColumnIndex];
        rowData[0] = lecture.major;
        rowData[1] = String.valueOf(lecture.grade);
        rowData[2] = lecture.lectureNum;
        rowData[3] = lecture.name;
        rowData[4] = lecture.professor;
        rowData[5] = String.valueOf(lecture.score);
        rowData[6] = lectureTime(lecture.time);
        rowData[7] = lecture.classRoom;
        rowData[8] = String.valueOf(lecture.enrolled);
        rowData[9] = String.valueOf(lecture.max);

        for (Integer columnIndex : userActionButtonColumns) {
            String columnName = columnNames.get(columnIndex);
            JButton button = createUserControlButton(lecture, columnName, userActions.get(columnIndex));
            button.setText(Translator.getBundleString(columnName));
            rowData[columnIndex] = button;
        }
        for (Integer columnIndex : actionButtonColumns) {
            String columnName = columnNames.get(columnIndex);
            JButton button = createButton(lecture, columnName, actions.get(columnIndex));
            button.setText(Translator.getBundleString(columnName));
            rowData[columnIndex] = button;
        }

        return rowData;
    }

    private void setColumnWidth() {
        lectureTable.getColumn("lecture_name").setMinWidth(150);
        lectureTable.getColumn("lecture_time").setMinWidth(150);
        lectureTable.getColumn("lecture_num").setMinWidth(50);
    }

    private void addColumn(String columnName) {
        tableModel.addColumn(Translator.getBundleString(columnName));
        columnNames.put(lastColumnIndex, columnName);
        for (int columnIndex : userActionButtonColumns) {
            String identifier = tableModel.getColumnName(columnIndex);
            lectureTable.getColumn(identifier).setCellRenderer(buttonRenderer);
        }
        for (int columnIndex : actionButtonColumns) {
            String identifier = tableModel.getColumnName(columnIndex);
            lectureTable.getColumn(identifier).setCellRenderer(buttonRenderer);
        }
        setColumnWidth();
    }

    public void addButtonColumn(String columnName, UserAction userAction) {
        lastColumnIndex += 1;
        userActionButtonColumns.push(lastColumnIndex);

        addColumn(columnName);

        userActions.put(lastColumnIndex, userAction);
    }

    public void addButtonColumn(String columnName, Action action) {
        lastColumnIndex += 1;
        actionButtonColumns.push(lastColumnIndex);

        addColumn(columnName);

        actions.put(lastColumnIndex, action);
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

    public JButton createButton(Lecture lecture, String name, Action action) {
        JButton button = new JButton(name);
        button.addActionListener(event -> action.action(lecture));

        return button;
    }

    @FunctionalInterface
    public interface Action {
        void action(Lecture lecture);
    }

    @FunctionalInterface
    public interface UserAction {
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

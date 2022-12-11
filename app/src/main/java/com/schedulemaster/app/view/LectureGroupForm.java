package com.schedulemaster.app.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.schedulemaster.app.util.Translator;
import com.schedulemaster.app.view.table.LectureTableForm;
import com.schedulemaster.misc.LinkedList;
import com.schedulemaster.model.Lecture;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class LectureGroupForm extends ComponentForm {
    private JPanel panel;
    private JLabel groupNameLabel;
    private JPanel groupTablePanel;
    private JButton searchButton;
    private JPanel headerPanel;
    private JButton deleteButton;

    private final LectureTableForm lectureTableForm;
    private final SearchDialog searchDialog;
    private int groupNumber;

    public LectureGroupForm(MainFrame frame, LectureGroupListForm groupListForm, int groupNumber) {
        this.groupNumber = groupNumber;
        lectureTableForm = new LectureTableForm(frame);
        searchDialog = new SearchDialog(frame);
        $$$setupUI$$$();

        searchDialog.addCallBack(() -> lectureTableForm.addLectures(searchDialog.getSelectedLectures()));
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchDialog.setVisible(true);
            }
        });
        lectureTableForm.addButtonColumn("unselect", ((lecture) -> {
            lectureTableForm.removeLecture(lecture);
            lectureTableForm.updateView();
        }));
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                groupListForm.deleteGroup(LectureGroupForm.this);
            }
        });
        updateGroupNameLabel();
        addThemeChangeListener(lectureTableForm::onThemeChange);
        addThemeChangeListener(() -> SwingUtilities.updateComponentTreeUI(searchDialog));
        addLocaleChangeListener(() -> {
            searchButton.setText(Translator.getBundleString("search"));
            deleteButton.setText(Translator.getBundleString("delete_group"));
            updateGroupNameLabel();
            lectureTableForm.onLocaleChange();
            searchDialog.updateLocale();
        });
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
        updateGroupNameLabel();
    }

    private void updateGroupNameLabel() {
        String text = Translator.getBundleString("group") + " " + groupNumber;
        groupNameLabel.setText(text);
    }

    public LinkedList<Lecture> getLectures() {
        return lectureTableForm.getLectures();
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    private void createUIComponents() {
        groupNameLabel = new HeaderLabel();
        groupNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        groupTablePanel = lectureTableForm.getPanel();
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
        panel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(groupTablePanel, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(headerPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        headerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        groupNameLabel.setText("Label");
        headerPanel.add(groupNameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        headerPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        searchButton = new JButton();
        this.$$$loadButtonText$$$(searchButton, this.$$$getMessageFromBundle$$$("string", "search"));
        headerPanel.add(searchButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        this.$$$loadButtonText$$$(deleteButton, this.$$$getMessageFromBundle$$$("string", "delete_group"));
        headerPanel.add(deleteButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}

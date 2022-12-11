package com.schedulemaster.app.view.content;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.schedulemaster.app.controller.MagicController;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.model.Schedule;
import com.schedulemaster.app.util.Translator;
import com.schedulemaster.app.view.HeaderLabel;
import com.schedulemaster.app.view.MainFrame;
import com.schedulemaster.app.view.TimeTableForm;
import com.schedulemaster.app.view.table.LectureTableForm;
import com.schedulemaster.model.Lecture;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class MagicSelectorForm extends ContentForm {
    private JPanel panel;
    private JButton previousButton;
    private JButton nextButton;
    private JButton selectButton;
    private JPanel timeTablePanel;
    private JPanel lectureTablePanel;
    private JLabel previewLabel;

    private final MainFrame frame;
    private final LectureTableForm lectureTableForm;
    private final TimeTableForm timeTableForm = new TimeTableForm();
    private Schedule[] schedules;
    private int selectedScheduleIndex = 0;

    public MagicSelectorForm(MainFrame frame) {
        this.frame = frame;
        lectureTableForm = new LectureTableForm(frame);
        $$$setupUI$$$();
        previousButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectSchedule(selectedScheduleIndex - 1);
            }
        });
        nextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectSchedule(selectedScheduleIndex + 1);
            }
        });
        selectButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    UserController userController = frame.getUserController();
                    for (Lecture lecture : schedules[selectedScheduleIndex].getLectures()) {
                        userController.selectLecture(lecture);
                    }
                    frame.setContentForm(Content.Home);
                } catch (IOException ex) {
                    String title = ResourceBundle.getBundle(MainFrame.RESOURCE_BUNDLE_NAME).getString("error");
                    JOptionPane.showMessageDialog(frame, ex.getLocalizedMessage(), title, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addComponentForm(lectureTableForm);
        addComponentForm(timeTableForm);

        addLocaleChangeListener(() -> {
            selectButton.setText(Translator.getBundleString("select_btn"));
            previewLabel.setText(Translator.getBundleString("preview_label"));
            previousButton.setText(Translator.getBundleString("previous_btn"));
            nextButton.setText(Translator.getBundleString("next_btn"));
        });
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void load() {
        try {
            lectureTableForm.clear();
            timeTableForm.clear();
            selectedScheduleIndex = 0;
            MagicController magicController = frame.getMagicController();
            magicController.magic();
            schedules = magicController.getSchedules();
            if (schedules.length < 1) {
                String title = ResourceBundle.getBundle(MainFrame.RESOURCE_BUNDLE_NAME).getString("info");
                String msg = ResourceBundle.getBundle(MainFrame.RESOURCE_BUNDLE_NAME).getString("no_available_schedule");
                JOptionPane.showMessageDialog(frame, msg, title, JOptionPane.ERROR_MESSAGE);
                frame.setContentForm(Content.MagicWizard);
                return;
            }
            lectureTableForm.setLectures(schedules[0].getLectures());
            timeTableForm.setLectures(schedules[0].getLectures());
        } catch (IOException e) {
            String title = ResourceBundle.getBundle(MainFrame.RESOURCE_BUNDLE_NAME).getString("error");
            JOptionPane.showMessageDialog(frame, e.getLocalizedMessage(), title, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void selectSchedule(int scheduleIndex) {
        if (!(0 <= scheduleIndex && scheduleIndex < schedules.length))
            return;
        selectedScheduleIndex = scheduleIndex;
        lectureTableForm.setLectures(schedules[selectedScheduleIndex].getLectures());
        timeTableForm.setLectures(schedules[selectedScheduleIndex].getLectures());
    }

    private void createUIComponents() {
        previewLabel = new HeaderLabel();
        previewLabel.setBorder(BorderFactory.createEmptyBorder(20, 35, 0, 5));

        lectureTablePanel = lectureTableForm.getPanel();
        timeTablePanel = timeTableForm.getPanel();
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
        panel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        previousButton = new JButton();
        this.$$$loadButtonText$$$(previousButton, this.$$$getMessageFromBundle$$$("string", "previous_btn"));
        panel2.add(previousButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextButton = new JButton();
        this.$$$loadButtonText$$$(nextButton, this.$$$getMessageFromBundle$$$("string", "next_btn"));
        panel2.add(nextButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        selectButton = new JButton();
        this.$$$loadButtonText$$$(selectButton, this.$$$getMessageFromBundle$$$("string", "select_btn"));
        panel2.add(selectButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(lectureTablePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.add(timeTablePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        this.$$$loadLabelText$$$(previewLabel, this.$$$getMessageFromBundle$$$("string", "preview_label"));
        panel3.add(previewLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
    private void $$$loadLabelText$$$(JLabel component, String text) {
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
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
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

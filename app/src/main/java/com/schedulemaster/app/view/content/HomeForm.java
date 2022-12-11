package com.schedulemaster.app.view.content;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.util.Translator;
import com.schedulemaster.app.view.HeaderLabel;
import com.schedulemaster.app.view.MainFrame;
import com.schedulemaster.app.view.TimeTableForm;
import com.schedulemaster.app.view.table.HomeLectureTableForm;
import com.schedulemaster.app.view.table.LectureTableForm;
import com.schedulemaster.app.view.table.SelectedLectureTableForm;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class HomeForm extends ContentForm {
    private final MainFrame frame;
    private JPanel panel;
    private JPanel timeTablePanel;
    private JPanel selectedTablePanel;
    private JLabel lectureBagLabel;
    private JLabel timeTableLabel;
    private JPanel searchFormPanel;
    private final LectureTableForm selectedLectureTableForm;
    private final SearchForm searchForm;
    private final TimeTableForm timeTableForm = new TimeTableForm();

    public HomeForm(MainFrame frame) {
        this.frame = frame;
        selectedLectureTableForm = new SelectedLectureTableForm(frame);
        searchForm = new SearchForm(frame, new HomeLectureTableForm(frame));

        $$$setupUI$$$();

        lectureBagLabel.setBorder(BorderFactory.createEmptyBorder(5, 35, 0, 5));
        timeTableLabel.setBorder(BorderFactory.createEmptyBorder(20, 35, 0, 5));

        addComponentForm(selectedLectureTableForm);
        addComponentForm(searchForm);
        addComponentForm(timeTableForm);

        addLocaleChangeListener(() -> {
            lectureBagLabel.setText(Translator.getBundleString("lecture_bag"));
            timeTableLabel.setText(Translator.getBundleString("lecture_bag"));
        });
    }

    @Override
    public void load() {
        UserController userController = frame.getUserController();

        selectedLectureTableForm.setLectures(userController.getSelectedLectures());

        timeTableForm.setLectures(userController.getSelectedLectures());

        frame.addSelectedLectureView(selectedLectureTableForm);
        frame.addSelectedLectureView(timeTableForm);

        searchForm.load();
    }

    @Override
    public JPanel getPanel() {
        return panel;
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
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.add(selectedTablePanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        this.$$$loadLabelText$$$(lectureBagLabel, this.$$$getMessageFromBundle$$$("string", "lecture_bag"));
        panel1.add(lectureBagLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(searchFormPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.add(timeTablePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        this.$$$loadLabelText$$$(timeTableLabel, this.$$$getMessageFromBundle$$$("string", "lecture_bag"));
        panel2.add(timeTableLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

    private void createUIComponents() {
        timeTablePanel = timeTableForm.getPanel();
        selectedTablePanel = selectedLectureTableForm.getPanel();
        searchFormPanel = searchForm.getPanel();

        lectureBagLabel = new HeaderLabel();
        timeTableLabel = new HeaderLabel();
    }
}

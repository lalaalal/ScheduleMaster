package com.schedulemaster.app.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.schedulemaster.app.Client;
import com.schedulemaster.app.controller.LectureController;
import com.schedulemaster.app.controller.MagicController;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.observers.EnrolledLectureObserver;
import com.schedulemaster.app.observers.LectureBookObserver;
import com.schedulemaster.app.observers.SelectedLectureObserver;
import com.schedulemaster.misc.Hash;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class MainFrame extends JFrame {
    private final LoginFrom loginFrom = new LoginFrom(this);

    private final Hash<ContentForm.Content, ContentForm> contentForms = new Hash<>();
    private JPanel mainPanel;
    private JPanel titleBar;
    private JPanel contentPanel;
    private JLabel titleLabel;
    private JLabel userIDLabel;
    private JLabel logoutLabel;
    private JLabel homeLabel;
    private JLabel lectureBagLabel;
    private JLabel wizardLabel;

    private Client client;
    private LectureController lectureController;
    private UserController userController;
    private MagicController magicController;
    private final EnrolledLectureObserver enrolledLectureObserver = new EnrolledLectureObserver(this);
    private final SelectedLectureObserver selectedLectureObserver = new SelectedLectureObserver(this);
    private final LectureBookObserver lectureBookObserver = new LectureBookObserver(this);

    public static final String RESOURCE_BUNDLE_NAME = "string";

    public MainFrame() {
        super("Main");

        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(loginFrom.getPanel());
        setLocationRelativeTo(null);

        titleBar.setBorder(new EmptyBorder(20, 20, 20, 20));
        logoutLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        setVisible(true);
        logoutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                disconnectServer();
                showLoginForm();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnectServer();
            }
        });

        contentForms.set(ContentForm.Content.Home, new HomeForm(this));
        contentForms.set(ContentForm.Content.LectureBag, new LectureBagForm(this));
        contentForms.set(ContentForm.Content.MagicWizard, new MagicWizardForm(this));
        contentForms.set(ContentForm.Content.MagicSelector, new MagicSelectorForm(this));
        homeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setContentForm(ContentForm.Content.Home);
            }
        });
        lectureBagLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setContentForm(ContentForm.Content.LectureBag);
            }
        });
        wizardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setContentForm(ContentForm.Content.MagicWizard);
            }
        });


    }

    public void setContentForm(ContentForm.Content content) {
        ContentForm contentForm = contentForms.get(content);
        if (contentForm == null)
            return;

        contentPanel.removeAll();
        contentPanel.add(contentForm.getPanel(), BorderLayout.CENTER);

        contentForm.load();
        revalidate();
        repaint();
    }

    public boolean connectServer() {
        if (client != null)
            return true;
        try {
            client = new Client();
            lectureController = new LectureController(client);
            userController = new UserController(client, lectureController.getLectureBook());
            magicController = new MagicController(userController, lectureController.getLectureBook());
            lectureController.addObserver(lectureBookObserver);
            lectureController.addObserver(selectedLectureObserver);
            lectureController.addObserver(enrolledLectureObserver);
            userController.addObserver(lectureBookObserver);
            userController.addObserver(selectedLectureObserver);
            userController.addObserver(enrolledLectureObserver);
            return true;
        } catch (IOException e) {
            String msg = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("connection_fail");
            String title = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("error");
            JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
            client = null;
            return false;
        }
    }

    private void disconnectServer() {
        try {
            if (client != null) {
                client.close();
                client = null;
            }
            if (userController != null)
                userController.logout();
        } catch (IOException e) {
            String msg = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("disconnection_fail");
            String title = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("error");
            JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
            client = null;
        }
    }

    public void login() {
        setContentPane(mainPanel);
        setContentForm(ContentForm.Content.Home);
        revalidate();
        repaint();
    }

    private void showLoginForm() {
        setContentPane(loginFrom.getPanel());
        revalidate();
        repaint();
    }

    public void setUserID(String id) {
        userIDLabel.setText(id);
    }

    public LectureController getLectureController() {
        return lectureController;
    }

    public UserController getUserController() {
        return userController;
    }

    public MagicController getMagicController() {
        return magicController;
    }

    public void addEnrolledLectureView(LectureView lectureView) {
        enrolledLectureObserver.addLectureView(lectureView);
    }

    public void addSelectedLectureView(LectureView lectureView) {
        selectedLectureObserver.addLectureView(lectureView);
    }

    public void addBookLectureView(LectureView lectureView) {
        lectureBookObserver.addLectureView(lectureView);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        titleBar = new JPanel();
        titleBar.setLayout(new GridLayoutManager(1, 9, new Insets(0, 0, 0, 0), -1, -1));
        titleBar.setBackground(new Color(-13156691));
        titleBar.setForeground(new Color(-13156691));
        mainPanel.add(titleBar, BorderLayout.NORTH);
        titleLabel = new JLabel();
        titleLabel.setForeground(new Color(-1));
        this.$$$loadLabelText$$$(titleLabel, this.$$$getMessageFromBundle$$$("string", "title"));
        titleBar.add(titleLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userIDLabel = new JLabel();
        userIDLabel.setForeground(new Color(-1));
        this.$$$loadLabelText$$$(userIDLabel, this.$$$getMessageFromBundle$$$("string", "default_id"));
        titleBar.add(userIDLabel, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logoutLabel = new JLabel();
        logoutLabel.setForeground(new Color(-1));
        this.$$$loadLabelText$$$(logoutLabel, this.$$$getMessageFromBundle$$$("string", "logout"));
        titleBar.add(logoutLabel, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        titleBar.add(spacer1, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        titleBar.add(spacer2, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(15, -1), null, null, 0, false));
        homeLabel = new JLabel();
        homeLabel.setForeground(new Color(-1));
        this.$$$loadLabelText$$$(homeLabel, this.$$$getMessageFromBundle$$$("string", "home"));
        titleBar.add(homeLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        titleBar.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(15, -1), null, null, 0, false));
        lectureBagLabel = new JLabel();
        lectureBagLabel.setForeground(new Color(-1));
        this.$$$loadLabelText$$$(lectureBagLabel, this.$$$getMessageFromBundle$$$("string", "enrolled"));
        titleBar.add(lectureBagLabel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        wizardLabel = new JLabel();
        wizardLabel.setForeground(new Color(-1));
        this.$$$loadLabelText$$$(wizardLabel, this.$$$getMessageFromBundle$$$("string", "wizard"));
        titleBar.add(wizardLabel, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
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
        return mainPanel;
    }

}

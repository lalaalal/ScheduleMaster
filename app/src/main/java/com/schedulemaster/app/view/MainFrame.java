package com.schedulemaster.app.view;

import com.schedulemaster.app.Client;
import com.schedulemaster.app.controller.LectureController;
import com.schedulemaster.app.controller.MagicController;
import com.schedulemaster.app.controller.UserController;
import com.schedulemaster.app.observers.EnrolledLectureObserver;
import com.schedulemaster.app.observers.LectureBookObserver;
import com.schedulemaster.app.observers.SelectedLectureObserver;
import com.schedulemaster.app.view.content.*;
import com.schedulemaster.misc.Hash;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;
import mdlaf.themes.MaterialOceanicTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFrame extends JFrame {
    private static final Hash<String, Locale> SUPPORTED_LOCALES = new Hash<>();
    private static final LookAndFeel LITE_THEME = new MaterialLookAndFeel(new MaterialLiteTheme());
    private static final LookAndFeel DARK_THEME = new MaterialLookAndFeel(new MaterialOceanicTheme());
    private final LoginFrom loginFrom = new LoginFrom(this);
    private AppTheme theme = AppTheme.Lite;
    private final Hash<ContentForm.Content, ContentForm> contentForms = new Hash<>();
    private final TitleBarForm titleBarForm = new TitleBarForm(this);

    private JPanel mainPanel;
    private JPanel titleBar;
    private JPanel contentPanel;

    private Client client;
    private LectureController lectureController;
    private UserController userController;
    private MagicController magicController;
    private final EnrolledLectureObserver enrolledLectureObserver = new EnrolledLectureObserver(this);
    private final SelectedLectureObserver selectedLectureObserver = new SelectedLectureObserver(this);
    private final LectureBookObserver lectureBookObserver = new LectureBookObserver(this);

    public static final String RESOURCE_BUNDLE_NAME = "string";

    static {
        SUPPORTED_LOCALES.put("korean", Locale.KOREAN);
        SUPPORTED_LOCALES.put("english", Locale.ENGLISH);
        SUPPORTED_LOCALES.put("japanese", Locale.JAPANESE);
        SUPPORTED_LOCALES.put("chinese", Locale.CHINESE);
    }

    public static Iterable<String> supportedLocaleKeys() {
        return SUPPORTED_LOCALES.getKeys();
    }

    public MainFrame() {
        super("Main");

        $$$setupUI$$$();
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(loginFrom.getPanel());
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnectServer();
            }
        });

        contentForms.set(ContentForm.Content.Login, loginFrom);
        contentForms.set(ContentForm.Content.Home, new HomeForm(this));
        contentForms.set(ContentForm.Content.LectureBag, new LectureBagForm(this));
        contentForms.set(ContentForm.Content.MagicWizard, new MagicWizardForm(this));
        contentForms.set(ContentForm.Content.MagicSelector, new MagicSelectorForm(this));
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

    protected void disconnectServer() {
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

    protected void showLoginForm() {
        setContentPane(loginFrom.getPanel());
        revalidate();
        repaint();
    }

    public void setUserID(String id) {
        titleBarForm.setUserID(id);
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

    public void changeLocale(Locale locale) {
        Locale.setDefault(locale);
        for (ContentForm contentForm : contentForms)
            contentForm.onLocaleChange();
    }

    /**
     * Change Application theme. If you have another component in MainFrame, override and call ohThemeChange.
     *
     * @param theme Theme to change
     */
    public void setTheme(AppTheme theme) {
        try {
            this.theme = theme;
            if (theme == AppTheme.Lite)
                UIManager.setLookAndFeel(LITE_THEME);
            else
                UIManager.setLookAndFeel(DARK_THEME);
            for (ContentForm contentForm : contentForms)
                contentForm.onThemeChange();
        } catch (UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Toggle theme between lite and dark.
     */
    public void toggleTheme() {
        if (theme == AppTheme.Lite)
            setTheme(AppTheme.Dark);
        else
            setTheme(AppTheme.Lite);
    }

    private void createUIComponents() {
        titleBar = titleBarForm.getPanel();
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
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(titleBar, BorderLayout.NORTH);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}

package com.schedulemaster.app;

import com.schedulemaster.app.util.FontManager;
import com.schedulemaster.app.view.MainFrame;
import mdlaf.MaterialLookAndFeel;

import javax.swing.*;
import java.util.Enumeration;
import java.util.Locale;

public class App {
    public static void setSystemProperties() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    public static void main(String[] args) {
        try {
            setSystemProperties();
            Locale.setDefault(Locale.KOREAN);
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
            FontManager.loadFont();
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

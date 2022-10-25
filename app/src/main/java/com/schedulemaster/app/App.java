package com.schedulemaster.app;

import com.schedulemaster.app.view.MainFrame;
import mdlaf.MaterialLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Locale;

public class App {
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
            Locale.setDefault(Locale.KOREAN);
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
            setUIFont (new javax.swing.plaf.FontUIResource("Sans", Font.PLAIN,12));
            MainFrame frame = new MainFrame();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}

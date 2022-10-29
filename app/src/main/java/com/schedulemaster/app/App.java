package com.schedulemaster.app;

import com.schedulemaster.app.view.MainFrame;
import mdlaf.MaterialLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
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
            try (InputStream is = App.class.getClassLoader().getResourceAsStream("font/SCDream4.otf")) {
                if (is == null)
                    throw new IOException("Font not found");
                Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(12f);
                setUIFont (new javax.swing.plaf.FontUIResource(font));
            }
            MainFrame frame = new MainFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

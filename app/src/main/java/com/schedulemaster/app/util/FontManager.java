package com.schedulemaster.app.util;


import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * Provide some fonts and set default font.
 *
 * @author lalaalal
 */
public class FontManager {
    public static final float DEFAULT_FONT_SIZE = 13f;
    public static final float HEADER_FONT_SIZE = 18f;

    /**
     * Get font with size.
     *
     * @param size Font size
     * @return Sized font
     */
    public static Font getFont(float size) {
        return UIManager.getDefaults().getFont("Label.font").deriveFont(size);
    }

    /**
     * Set default font to SCDream in resources. Call after set LookAndFeels.
     *
     * @throws IOException         If font file not found
     * @throws FontFormatException If the fontStream data does not contain the required font tables for the specified format
     */
    public static void loadFont() throws IOException, FontFormatException {
        try (InputStream is = FontManager.class.getClassLoader().getResourceAsStream("font/SCDream4.otf")) {
            if (is == null)
                throw new IOException("Font not found");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(DEFAULT_FONT_SIZE);
            setUIFont(new FontUIResource(font));
        }
    }

    /**
     * Set default font to param. Call after set LookAndFeels.
     *
     * @param fontUIResource Font resource to set as default font
     */
    public static void setUIFont(FontUIResource fontUIResource) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, fontUIResource);
        }
    }
}


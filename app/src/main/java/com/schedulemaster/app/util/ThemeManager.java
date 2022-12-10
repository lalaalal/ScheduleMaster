package com.schedulemaster.app.util;

import com.schedulemaster.app.view.AppTheme;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;
import mdlaf.themes.MaterialOceanicTheme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Handle theme colors. Singleton
 *
 * @author lalaalal
 */
public class ThemeManager {
    public static final LookAndFeel LITE_THEME = new MaterialLookAndFeel(new MaterialLiteTheme());
    public static final LookAndFeel DARK_THEME = new MaterialLookAndFeel(new MaterialOceanicTheme());
    private static ThemeManager instance;
    public AppTheme currentTheme;
    private final HashMap<String, Color> liteColor = new HashMap<>();
    private final HashMap<String, Color> darkColor = new HashMap<>();

    /**
     * Ensure only one instance exists.
     *
     * @return Instance of ThemeManager
     */
    public static ThemeManager getInstance() {
        if (instance == null)
            return instance = new ThemeManager();
        return instance;
    }

    /**
     * Get current theme default color.
     *
     * @param key Color key
     * @return Default color
     */
    public static Color getDefaultColor(String key) {
        return UIManager.getDefaults().getColor(key);
    }

    /**
     * Get default theme color.
     *
     * @param lookAndFeel Theme
     * @param key         Color key
     * @return Default color
     */
    public static Color getDefaultColor(LookAndFeel lookAndFeel, String key) {
        return lookAndFeel.getDefaults().getColor(key);
    }

    private ThemeManager() {
        addColor(AppTheme.Lite, "Table.cellBorder", Color.LIGHT_GRAY);
        addColor(AppTheme.Dark, "Table.cellBorder", Color.DARK_GRAY);

        addColor(AppTheme.Lite, "TitleBar.background", Color.decode("#373EAD"));
        addColor(AppTheme.Lite, "TitleBar.foreground", Color.LIGHT_GRAY);
        addColor(AppTheme.Dark, "TitleBar.background", getDefaultColor(DARK_THEME, "Table.selectionBackground"));
        addColor(AppTheme.Dark, "TitleBar.foreground", getDefaultColor(DARK_THEME, "Label.foreground"));
    }

    /**
     * Add theme color.
     *
     * @param theme Application Theme
     * @param key   Color key
     * @param color Color
     */
    public void addColor(AppTheme theme, String key, Color color) {
        if (theme == AppTheme.Lite)
            liteColor.put(key, color);
        else
            darkColor.put(key, color);
    }

    /**
     * Get defined color or theme default color.
     *
     * @param key Color key
     * @return Color
     */
    public Color getColor(String key) {
        if (currentTheme == AppTheme.Lite && liteColor.containsKey(key))
            return liteColor.get(key);
        if (currentTheme == AppTheme.Dark && darkColor.containsKey(key))
            return darkColor.get(key);
        return getDefaultColor(key);
    }
}

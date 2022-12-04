package com.schedulemaster.app.view;

import com.schedulemaster.misc.LinkedList;

import javax.swing.*;

public abstract class ComponentForm {
    private final LinkedList<LocaleChangeListener> localeChangeListeners = new LinkedList<>();
    private final LinkedList<ThemeChangeListener> themeChangeListeners = new LinkedList<>();

    public ComponentForm() {
        themeChangeListeners.push(() -> SwingUtilities.updateComponentTreeUI(getPanel()));
    }

    public abstract JPanel getPanel();

    public void onLocaleChange() {
        for (LocaleChangeListener localeChangeListener : localeChangeListeners)
            localeChangeListener.onLocaleChange();
    }

    public void onThemeChange() {
        for (ThemeChangeListener themeChangeListener : themeChangeListeners)
            themeChangeListener.onThemeChange();
    }
}

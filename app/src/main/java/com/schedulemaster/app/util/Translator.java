package com.schedulemaster.app.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Application Translation.
 *
 * @author lalaalal
 */
public class Translator {
    /**
     * Get translated string from resource bundle.
     *
     * @param key Key of translated string
     * @return Value matching key, key if not exists
     */
    public static String getBundleString(String key) {
        try {
            return ResourceBundle.getBundle("string").getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }
}

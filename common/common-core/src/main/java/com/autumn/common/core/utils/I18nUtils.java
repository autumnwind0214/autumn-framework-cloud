package com.autumn.common.core.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化工具类
 * @author autumn
 */
public class I18nUtils {
    public static String getMessage(String key, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString(key);
    }
}

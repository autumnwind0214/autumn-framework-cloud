package com.autumn.common.core.utils;

import com.autumn.common.core.enums.LanguageEnum;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化工具类
 * @author autumn
 */
public class I18nUtils {

    public static String getMessage(String key, String label) {
        Locale locale = LanguageEnum.getByLocale(label);
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString(key);
    }
}

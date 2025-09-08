package com.autumn.common.core.utils;

import com.autumn.common.core.enums.LanguageEnum;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化工具类
 * @author autumn
 */
public class I18nUtils {

    public final static String ROLE_NOT_EXIST = "ROLE_NOT_EXIST";
    public final static String ROLE_NOT_EMPTY = "ROLE_NOT_EMPTY";
    public final static String MENU_NAME_EXIST = "MENU_NAME_EXIST";
    public final static String BAN_DISABLED_USER = "BAN_DISABLED_USER";

    public static String getMessage(String key, String label) {
        Locale locale = LanguageEnum.getByLocale(label);
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString(key);
    }
}

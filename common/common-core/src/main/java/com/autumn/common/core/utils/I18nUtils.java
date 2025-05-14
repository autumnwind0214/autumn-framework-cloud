package com.autumn.common.core.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author autumn
 * @desc 国际化工具类
 * @date 2025年05月14日
 */
public class I18nUtils {
    public static String getMessage(String key, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString(key);
    }

    public static void main(String[] args) {
        System.out.println(getMessage("CAPTCHA_NOT_EMPTY", Locale.CHINA));
    }
}

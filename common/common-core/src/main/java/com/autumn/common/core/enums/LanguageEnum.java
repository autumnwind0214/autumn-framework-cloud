package com.autumn.common.core.enums;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum LanguageEnum {
    ZH_CN("简体中文", Locale.CHINA),
    ZH_TW("繁體中文", Locale.TAIWAN),
    EN_US("English", Locale.ENGLISH),
    ;

    private final String label;
    private final Locale locale;

    LanguageEnum(String label, Locale locale) {
        this.label = label;
        this.locale = locale;
    }

    public static Locale getByLocale(String  locale) {
        for (LanguageEnum value : values()) {
            if (value.getLabel().equals(locale)) {
                return value.locale;
            }
        }
        return ZH_CN.getLocale();
    }
}


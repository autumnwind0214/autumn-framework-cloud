package com.autumn.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 徽标颜色集合
 */
@Getter
public enum BadgeVariantsEnum {

    DEFAULT("default"),
    DESTRUCTIVE("'destructive'"),
    PRIMARY("primary"),
    SUCCESS("success"),
    WARNING("warning"),
    ;

    @EnumValue
    private final String code;


    BadgeVariantsEnum(String code) {
        this.code = code;
    }
}

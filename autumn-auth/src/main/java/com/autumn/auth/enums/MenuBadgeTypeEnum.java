package com.autumn.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum MenuBadgeTypeEnum {
    none("node"),
    dot("dot"),
    normal("normal"),

    ;

    @EnumValue
    private final String code;


    MenuBadgeTypeEnum(String code) {
        this.code = code;
    }
}

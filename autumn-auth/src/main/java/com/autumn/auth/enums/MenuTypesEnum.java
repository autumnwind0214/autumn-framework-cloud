package com.autumn.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 菜单类型
 * @author autumn
 */
@Getter
public enum MenuTypesEnum {
    CATALOG("catalog", "目录"),
    MENU("menu", "菜单"),
    EMBEDDED("embedded", "内嵌"),
    LINK("link", "外链"),
    BUTTON("button", "按钮"),
    ;

    @EnumValue
    private final String code;
    private final String msg;


    MenuTypesEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

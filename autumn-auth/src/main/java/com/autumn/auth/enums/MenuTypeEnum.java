package com.autumn.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author autumn
 * @desc 菜单类型
 * @date 2025年05月13日
 */
@Getter
public enum MenuTypeEnum {
    MENU(0, "菜单"),
    IFRAME(1, "iframe"),
    BACKLINK(2, "外链"),
    BUTTON(3, "按钮"),
    ;

    @EnumValue
    private final Integer code;
    private final String msg;


    MenuTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

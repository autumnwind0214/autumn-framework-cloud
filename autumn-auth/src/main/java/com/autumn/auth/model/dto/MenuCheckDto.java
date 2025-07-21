package com.autumn.auth.model.dto;

import lombok.Data;

/**
 * 菜单检测参数
 */
@Data
public class MenuCheckDto {

    /**
     * 菜单名称
     */
    String name;

    /**
     * 组件路径
     */
    String component;
}

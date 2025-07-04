package com.autumn.auth.model.vo;

import com.autumn.common.core.base.TreeAble;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单树形结构
 * @author autumn
 */
@Data
public class MenuTreeVo implements TreeAble<MenuTreeVo>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;

    // 层级
    private Integer deep;

    // 排序
    private Integer sort;

    // title
    private String title;

    // 菜单类型
    private Integer menuType;

    // 按钮权限
    private String permissions;

    // 子级
    private List<MenuTreeVo> children;
}

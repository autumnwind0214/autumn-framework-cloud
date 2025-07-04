package com.autumn.auth.model.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色菜单
 * @author autumn
 */
@AutoMapper(target = MenuVo.class)
@Data
public class RoleMenuVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 菜单父节点id
     */
    private Long parentId;

    /**
     * 菜单id
     */
    private Long id;

    /**
     * 菜单类型 0.菜单    1.iframe    2.外链    3.按钮
     */
    private Integer menuType;

    /**
     * 菜单title
     */
    private String title;

}

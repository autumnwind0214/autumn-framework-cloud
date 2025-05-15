package com.autumn.auth.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author autumn
 * @desc 动态路由返回结果
 * @date 2024年11月15日
 */
@Data
public class RouteVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 路径
    private String path;

    // 路由名称
    private String name;

    // 元数据
    private Meta meta;

    private List<RouteVo> children;

    @Data
    public static class Meta implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        // 菜单和面包屑对应的图标
        private String icon;

        // 路由标题 (用作 document.title || 菜单的名称)
        private String title;

        // 排序
        private Integer sort;

        // 是否在菜单中显示
        private Boolean showLink;

        private String[] auths;
    }
}

package com.autumn.auth.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 动态路由返回结果
 *
 * @author autumn
 */
@Data
public class DynamicRouteVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long parentId;

    // 路径
    private String path;

    // 路由名称
    private String name;

    private String component;

    private String type;

    private Integer status;

    private String authCode;

    // 元数据
    private Meta meta;

    private List<DynamicRouteVo> children;

    @Data
    public static class Meta implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String badge;

        private String badgeVariants;

        private String badgeType;

        private Integer sort;

        private String title;

        private String icon;

        private String link;
    }
}

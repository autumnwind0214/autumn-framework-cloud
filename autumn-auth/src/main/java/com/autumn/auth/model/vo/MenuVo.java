package com.autumn.auth.model.vo;

import com.autumn.auth.entity.Menu;
import com.autumn.mybatis.handler.BooleanTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单返回值
 * @author autumn
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AutoMapper(target = Menu.class)
public class MenuVo {

    /**
     * id
     */
    Long id;

    /**
     * 父级id
     */
    Long parentId;

    /**
     * 路由名称
     */
    String name;

    /**
     * 状态
     */
    Integer status;

    /**
     * 类型
     */
    String type;

    /**
     * 路由路径
     */
    String path;

    /**
     * 组件路径
     */
    String component;

    /**
     * 权限码
     */
    String authCode;

    // 元数据
    private Meta meta;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuVo> children;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class Meta implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private String icon;

        private String title;

        private Boolean affixTab;

        private Integer sort;

        private String link;

        private String badgeType;
    }
}

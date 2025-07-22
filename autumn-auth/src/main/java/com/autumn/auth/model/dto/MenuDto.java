package com.autumn.auth.model.dto;

import com.autumn.auth.entity.Menu;
import com.autumn.mybatis.handler.BooleanTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author autumn
 */
@Data
@AutoMapper(target = Menu.class)
public class MenuDto {

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
     * activePath
     * 类型：string
     * 默认值：''
     * 用于配置当前激活的菜单，有时候页面没有显示在菜单内，需要激活父级菜单时使用。
     */
    String activePath;

    /**
     * 组件路径
     */
    String component;

    /**
     * 权限码
     */
    String authCode;

    /**
     * iframeSrc
     * 类型：string
     * 默认值：''
     * 用于配置内嵌页面的 iframe 地址，设置后会在当前页面内嵌对应的页面。
     */
    String iframeSrc;

    /**
     * link
     * 类型：string
     * 默认值：''
     * 用于配置外链跳转路径，会在新窗口打开。
     */
    String link;

    /**
     * sort
     * 类型：number
     * 默认值：0
     * 用于配置页面的排序，用于路由到菜单排序。
     * 注意: 排序仅针对一级菜单有效，二级菜单的排序需要在对应的一级菜单中按代码顺序设置。
     */
    Integer sort;

    Meta meta;

    @Data
    @AutoMapper(target = Menu.class)
    public static class Meta {
        /**
         * 菜单标题
         */
        String title;


        /**
         * 图标
         */
        String icon;

        /**
         * 激活图标
         */
        String activeIcon;

        /**
         * 用于配置页面的徽标类型，dot 为小红点，normal 为文本。
         * 类型：'dot' | 'normal'
         * default：'normal'
         */
        String badgeType;

        /**
         * 用于配置页面的徽标，会在菜单显示。
         * default：''
         */
        String badge;

        /**
         * 用于配置页面的徽标颜色。
         * 类型：'default' | 'destructive' | 'primary' | 'success' | 'warning' | string
         * default：success
         */
        String badgeVariants;

        /**
         * 用于配置页面是否开启缓存，开启后页面会缓存，不会重新加载，仅在标签页启用时有效。
         * default: false
         */
        Boolean keepAlive;

        /**
         * affixTab
         * 类型：boolean
         * 默认值：false
         * 用于配置页面是否固定标签页，固定后页面不可关闭。
         */
        Boolean affixTab;

        /**
         * 用于配置页面是否在菜单中隐藏，隐藏后页面不会在菜单中显示。
         * default: false
         */
        Boolean hideInMenu;

        /**
         * 用于配置页面的子页面是否在菜单中隐藏，隐藏后子页面不会在菜单中显示。
         * default: false
         */
        Boolean hideChildrenInMenu;

        /**
         * 用于配置页面是否在面包屑中隐藏，隐藏后页面不会在面包屑中显示。
         * default: false
         */
        Boolean hideInBreadcrumb;

        /**
         * 用于配置页面是否在标签页中隐藏，隐藏后页面不会在标签页中显示。
         * default: false
         */
        Boolean hideInTab;

    }
}

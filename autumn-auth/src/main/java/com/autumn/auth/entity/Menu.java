package com.autumn.auth.entity;

import com.autumn.auth.model.dto.MenuDto;
import com.autumn.auth.model.vo.DynamicRouteVo;
import com.autumn.auth.model.vo.RoleMenuVo;
import com.autumn.mybatis.core.model.BaseEntity;
import com.autumn.mybatis.handler.BooleanTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单
 *
 * @author autumn
 */
@AutoMappers({
        @AutoMapper(target = MenuDto.class),
        @AutoMapper(target = RoleMenuVo.class),
        @AutoMapper(target = DynamicRouteVo.class),
        @AutoMapper(target = DynamicRouteVo.Meta.class)
})
@EqualsAndHashCode(callSuper = true)
@Data
public class Menu extends BaseEntity {

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
     * 图标
     */
    String icon;

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

    /**
     * 菜单标题
     */
    String title;

    /**
     * 激活图标
     */
    String activeIcon;

    /**
     * 用于配置页面是否开启缓存，开启后页面会缓存，不会重新加载，仅在标签页启用时有效。
     * default: false
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean keepAlive;

    /**
     * 用于配置页面是否在菜单中隐藏，隐藏后页面不会在菜单中显示。
     * default: false
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean hideInMenu;

    /**
     * 用于配置页面是否在标签页中隐藏，隐藏后页面不会在标签页中显示。
     * default: false
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean hideInTab;

    /**
     * 用于配置页面是否在面包屑中隐藏，隐藏后页面不会在面包屑中显示。
     * default: false
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean hideInBreadcrumb;

    /**
     * 用于配置页面的子页面是否在菜单中隐藏，隐藏后子页面不会在菜单中显示。
     * default: false
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean hideChildrenInMenu;

    /**
     * 用于配置页面的徽标，会在菜单显示。
     * default：''
     */
    String badge;

    /**
     * 用于配置页面的徽标类型，dot 为小红点，normal 为文本。
     * 类型：'dot' | 'normal'
     * default：'normal'
     */
    String badgeType;

    /**
     * 用于配置页面的徽标颜色。
     * 类型：'default' | 'destructive' | 'primary' | 'success' | 'warning' | string
     * default：success
     */
    String badgeVariants;

    /**
     * fullPathKey
     * 类型：boolean
     * 默认值：true
     * 是否将路由的完整路径作为tab key（默认true）
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean fullPathKey;

    /**
     * activePath
     * 类型：string
     * 默认值：''
     * 用于配置当前激活的菜单，有时候页面没有显示在菜单内，需要激活父级菜单时使用。
     */
    String activePath;

    /**
     * affixTab
     * 类型：boolean
     * 默认值：false
     * 用于配置页面是否固定标签页，固定后页面不可关闭。
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean affixTab;

    /**
     * affixTabOrder
     * 类型：number
     * 默认值：0
     * 用于配置页面固定标签页的排序, 采用升序排序。
     */
    Integer affixTabOrder;

    /**
     * iframeSrc
     * 类型：string
     * 默认值：''
     * 用于配置内嵌页面的 iframe 地址，设置后会在当前页面内嵌对应的页面。
     */
    String iframeSrc;

    /**
     * ignoreAccess
     * 类型：boolean
     * 默认值：false
     * 用于配置页面是否忽略权限，直接可以访问。
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean ignoreAccess;

    /**
     * link
     * 类型：string
     * 默认值：''
     * 用于配置外链跳转路径，会在新窗口打开。
     */
    String link;

    /**
     * maxNumOfOpenTab
     * 类型：number
     * 默认值：-1
     * 用于配置标签页最大打开数量，设置后会在打开新标签页时自动关闭最早打开的标签页(仅在打开同名标签页时生效)。
     */
    Integer maxNumOfOpenTab;

    /**
     * menuVisibleWithForbidden
     * 类型：boolean
     * 默认值：false
     * 用于配置页面在菜单可以看到，但是访问会被重定向到403。
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean menuVisibleWithForbidden;

    /**
     * openInNewWindow
     * 类型：boolean
     * 默认值：false
     * 设置为 true 时，会在新窗口打开页面。
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean openInNewWindow;

    /**
     * sort
     * 类型：number
     * 默认值：0
     * 用于配置页面的排序，用于路由到菜单排序。
     * 注意: 排序仅针对一级菜单有效，二级菜单的排序需要在对应的一级菜单中按代码顺序设置。
     */
    Integer sort;

    /**
     * noBasicLayout
     * 类型：boolean
     * 默认值：false
     * 用于配置当前路由不使用基础布局，仅在顶级时生效。默认情况下，所有的路由都会被包裹在基础布局中（包含顶部以及侧边等导航部件），如果你的页面不需要这些部件，可以设置 noBasicLayout 为 true。
     */
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean noBasicLayout;
}

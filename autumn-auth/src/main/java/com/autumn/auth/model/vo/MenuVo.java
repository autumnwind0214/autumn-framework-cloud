package com.autumn.auth.model.vo;

import com.autumn.auth.entity.Menu;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

/**
 * 菜单返回值
 * @author autumn
 */
@Data
@AutoMapper(target = Menu.class)
public class MenuVo {
    Long id;

    Long parentId;
    /**
     * 菜单类型（`0`代表菜单、`1`代表`iframe`、`2`代表外链、`3`代表按钮）
     */
    Integer menuType;
    /**
     * 菜单名称（兼容国际化、非国际化，如果用国际化的写法就必须在根目录的`locales`文件夹下对应添加）
     */
    String title;
    /**
     * 路由名称（必须唯一并且和当前路由`component`字段对应的页面里用`defineOptions`包起来的`name`保持一致）
     */
    String name;
    /**
     * 路由路径
     */
    String path;
    /**
     * 组件路径（传`component`组件路径，那么`path`可以随便写，如果不传，`component`组件路径会跟`path`保持一致）
     */
    String component;
    /**
     * 菜单排序
     */
    Integer sort;
    /**
     * 路由重定向
     */
    String redirect;
    /**
     * 菜单图标
     */
    String icon;
    /**
     * 右侧图标
     */
    String extraIcon;
    /**
     * 进场动画（页面加载动画）
     */
    String enterTransition;
    /**
     * 离场动画（页面加载动画）
     */
    String leaveTransition;
    /**
     * 菜单激活（将某个菜单激活，主要用于通过`query`或`params`传参的路由，
     * 当它们通过配置`showLink: false`后不在菜单中显示，就不会有任何菜单高亮，
     * 而通过设置`activePath`指定激活菜单即可获得高亮，`activePath`为指定激活菜单的`path`）
     */
    String activePath;
    /**
     * 权限标识（按钮级别权限设置）
     */
    String auths;
    /**
     * 链接地址（需要内嵌的`iframe`链接地址）
     */
    String frameSrc;
    /**
     * 加载动画（内嵌的`iframe`页面是否开启首次加载动画）
     */
    Boolean frameLoading;
    /**
     * 缓存页面（是否缓存该路由页面，开启后会保存该页面的整体状态，刷新后会清空状态）
     */
    Boolean keepAlive;
    /**
     * 标签页（当前菜单名称或自定义信息禁止添加到标签页）
     */
    Boolean hiddenTag;
    /**
     * 固定标签页（当前菜单名称是否固定显示在标签页且不可关闭）
     */
    Boolean fixedTag;
    /**
     * 菜单（是否显示该菜单）
     */
    Boolean showLink;
    /**
     * 父级菜单（是否显示父级菜单）
     */
    Boolean showParent;
}

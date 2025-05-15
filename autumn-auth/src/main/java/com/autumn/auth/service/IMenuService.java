package com.autumn.auth.service;


import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.autumn.auth.entity.Menu;
import com.autumn.auth.model.dto.MenuDto;
import com.autumn.auth.model.vo.MenuVo;
import com.autumn.auth.model.vo.RoleMenuVo;
import com.autumn.auth.model.vo.RouteVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author autumn
 * @desc IMenuService
 * @date 2024年11月15日
 */
public interface IMenuService extends IService<Menu> {

    // 查询用户具有的菜单
    List<RouteVo> getAsyncRoutes(Long userId);

    List<MenuVo> getList();

    Boolean addMenu(MenuDto dto);

    MenuVo getMenu(Long id);

    Boolean updateMenu(MenuDto dto);

    List<RoleMenuVo> getRoleMenus();

    Boolean delete(Long id);

    void updateRoutesCache();
}

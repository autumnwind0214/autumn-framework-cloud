package com.autumn.auth.service;


import com.autumn.auth.entity.Menu;
import com.autumn.auth.model.dto.MenuCheckDto;
import com.autumn.auth.model.dto.MenuDto;
import com.autumn.auth.model.vo.MenuVo;
import com.autumn.auth.model.vo.RoleMenuVo;
import com.autumn.auth.model.vo.DynamicRouteVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author autumn
 */
public interface IMenuService extends IService<Menu> {

    // 查询用户具有的菜单
    List<DynamicRouteVo> getAsyncRoutes(Long userId);

    List<MenuVo> getList();

    Boolean addMenu(MenuDto dto);

    MenuVo getMenu(Long id);

    Boolean updateMenu(MenuDto dto);

    List<RoleMenuVo> getRoleMenus();

    Boolean delete(Long id);

    void updateRoutesCache();

    Boolean checkParamsUnique(MenuCheckDto dto);
}

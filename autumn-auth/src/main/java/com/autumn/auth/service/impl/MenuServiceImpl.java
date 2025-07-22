package com.autumn.auth.service.impl;


import cn.hutool.core.lang.Assert;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.auth.entity.Menu;
import com.autumn.auth.enums.MenuTypesEnum;
import com.autumn.auth.mapper.MenuMapper;
import com.autumn.auth.mapper.UserRoleMapper;
import com.autumn.auth.model.dto.MenuDto;
import com.autumn.auth.model.vo.MenuVo;
import com.autumn.auth.model.vo.RoleMenuVo;
import com.autumn.auth.model.vo.DynamicRouteVo;
import com.autumn.auth.service.IAuthorizationUserService;
import com.autumn.auth.service.IMenuService;
import com.autumn.common.core.utils.BeanCopyUtils;
import com.autumn.common.core.utils.MapstructUtils;
import com.autumn.common.core.utils.TreeBuilderUtils;
import com.autumn.common.redis.constant.RedisConstant;
import com.autumn.common.redis.core.RedisOperator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author autumn
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final MenuMapper menuMapper;

    private final UserRoleMapper userRoleMapper;

    private final RedisOperator<List<DynamicRouteVo>> redisOperator;

    private final CacheManager cacheManager;

    private Cache<String, List<DynamicRouteVo>> routeVoCache;

    private final IAuthorizationUserService authorizationUserService;

    @PostConstruct
    public void init() {
        QuickConfig qc = QuickConfig.newBuilder("routeVoCache")
                .cacheType(CacheType.BOTH)
                .syncLocal(true)
                .build();
        routeVoCache = cacheManager.getOrCreateCache(qc);
    }


    @Override
    public List<DynamicRouteVo> getAsyncRoutes(Long userId) {
        String key = RedisConstant.ASYNC_ROUTES_PREFIX_KEY + userId;
        List<DynamicRouteVo> routeList = routeVoCache.get(key);
        if (!CollectionUtils.isEmpty(routeList)) {
            return routeList;
        }
        routeList = getRouteList(userId);
        routeVoCache.put(key, routeList);
        return routeList;
    }

    @Override
    public List<MenuVo> getList() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getSort);
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        List<MenuVo> list = menus.stream().map(menu -> {
            MenuVo vo = new MenuVo();
            MapstructUtils.convert(menu, vo);
            MenuVo.Meta meta = new MenuVo.Meta();
            MapstructUtils.convert(menu, meta);
            vo.setMeta(meta);
            return vo;
        }).toList();
        TreeBuilderUtils<MenuVo, Long> treeBuilder = new TreeBuilderUtils<>();
        // 菜单全部数据
        return treeBuilder.buildAndMapToData(list, MenuVo::getId, MenuVo::getParentId, 0L, MenuVo::setChildren);
    }

    @Override
    public MenuVo getMenu(Long id) {
        return menuMapper.selectVoById(id);
    }

    @Override
    public List<RoleMenuVo> getRoleMenus() {
        List<Menu> menus = menuMapper.selectList();
        return MapstructUtils.convert(menus, RoleMenuVo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, id);
        remove(wrapper);
        removeById(id);

        return removeById(id);
    }

    @Override
    public Boolean addMenu(MenuDto dto) {
        Assert.isTrue(checkPathUnique(null, dto.getPath()));
        Assert.isTrue(checkNameUnique(null, dto.getName()));
        Menu menu = new Menu();
        MapstructUtils.convert(dto, menu);
        MapstructUtils.convert(dto.getMeta(), menu);
        return this.save(menu);
    }

    @Override
    public Boolean updateMenu(MenuDto dto) {
        Assert.isTrue(checkPathUnique(dto.getId(), dto.getPath()));
        Assert.isTrue(checkNameUnique(dto.getId(), dto.getName()));
        Menu menu = new Menu();
        MapstructUtils.convert(dto, menu);
        MapstructUtils.convert(dto.getMeta(), menu);
        return this.updateById(menu);
    }

    /**
     * 更新路由缓存
     */
    @Override
    public void updateRoutesCache() {
        LambdaQueryWrapper<AuthorizationUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(AuthorizationUser::getId);
        authorizationUserService.list(wrapper).forEach(user -> {
            String key = RedisConstant.ASYNC_ROUTES_PREFIX_KEY + user.getId();
            if (!CollectionUtils.isEmpty(routeVoCache.get(key))) {
                List<DynamicRouteVo> list = getRouteList(user.getId());
                routeVoCache.put(key, list);
            }
        });
    }


    /**
     * 检查组件路径是否唯一
     *
     * @param path 组件路径
     * @return true:唯一
     */
    @Override
    public boolean checkPathUnique(Long menuId, String path) {
        if (!StringUtils.hasText(path)) {
            return true;
        }
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getPath, path);
        wrapper.ne(menuId != null, Menu::getId, menuId);
        return this.count(wrapper) <= 0;
    }

    /**
     * 检查菜单名称是否唯一
     *
     * @param name 菜单名称
     * @return true:唯一
     */
    @Override
    public boolean checkNameUnique(Long menuId, String name) {
        if (!StringUtils.hasText(name)) {
            return true;
        }
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getName, name);
        wrapper.ne(menuId != null, Menu::getId, menuId);
        return this.count(wrapper) <= 0;
    }

    /**
     * 获取路由表
     *
     * @param userId 用户id
     * @return 路由表
     */
    private List<DynamicRouteVo> getRouteList(Long userId) {
        List<DynamicRouteVo> routeList = new ArrayList<>();
        // 查询用户具有的menu_id
        List<Long> menuIds = userRoleMapper.queryMenuIdByUserId(userId);
        if (!CollectionUtils.isEmpty(menuIds)) {
            // 菜单全部数据(当前用户下的)
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.ne(Menu::getType, MenuTypesEnum.BUTTON.getCode());
            queryWrapper.in(Menu::getId, menuIds).orderByAsc(Menu::getSort);
            List<Menu> list = menuMapper.selectList(queryWrapper);
            List<DynamicRouteVo> routeVoList = list.stream().map(menu -> {
                        DynamicRouteVo vo = new DynamicRouteVo();
                        MapstructUtils.convert(menu, vo);
                        DynamicRouteVo.Meta meta = new DynamicRouteVo.Meta();
                        MapstructUtils.convert(menu, meta);
                        vo.setMeta(meta);
                        return vo;
                    }
            ).toList();
            TreeBuilderUtils<DynamicRouteVo, Long> treeBuilder = new TreeBuilderUtils<>();
            routeList = treeBuilder.buildAndMapToData(
                    routeVoList, DynamicRouteVo::getId, DynamicRouteVo::getParentId, 0L, DynamicRouteVo::setChildren);


        }

        return routeList;
    }
}

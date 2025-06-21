package com.autumn.auth.service.impl;


import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.auth.entity.Menu;
import com.autumn.auth.enums.MenuTypeEnum;
import com.autumn.auth.mapper.MenuMapper;
import com.autumn.auth.mapper.UserRoleMapper;
import com.autumn.auth.model.dto.MenuDto;
import com.autumn.auth.model.vo.MenuVo;
import com.autumn.auth.model.vo.RoleMenuVo;
import com.autumn.auth.model.vo.RouteVo;
import com.autumn.auth.service.IAuthorizationUserService;
import com.autumn.auth.service.IMenuService;
import com.autumn.auth.utils.SecurityUtils;
import com.autumn.common.core.utils.BeanCopyUtils;
import com.autumn.common.core.utils.MapstructUtils;
import com.autumn.common.redis.constant.RedisConstant;
import com.autumn.common.redis.core.RedisOperator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * @author autumn
 * @desc
 * @date 2024年11月15日
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final MenuMapper menuMapper;

    private final UserRoleMapper userRoleMapper;

    private final RedisOperator<List<RouteVo>> redisOperator;

    private final CacheManager cacheManager;

    private Cache<String, List<RouteVo>> routeVoCache;

    private final IAuthorizationUserService authorizationUserService;

    @PostConstruct
    public void init() {
        // QuickConfig qc = QuickConfig.newBuilder("routeVoCache")
        //         .cacheType(CacheType.BOTH)
        //         .syncLocal(true)
        //         .build();
        // routeVoCache = cacheManager.getOrCreateCache(qc);
    }


    @Override
    public List<RouteVo> getAsyncRoutes(Long userId) {
        String key = RedisConstant.ASYNC_ROUTES_PREFIX_KEY + userId;
        // List<RouteVo> routeList = routeVoCache.get(key);
        List<RouteVo> routeList = new ArrayList<>();
        // if (!CollectionUtils.isEmpty(routeList)) {
        //     return routeList;
        // }
        routeList = getRouteList(userId);
        // routeVoCache.put(key, routeList);
        return routeList;
    }

    @Override
    public List<MenuVo> getList() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getSort);
        // 菜单全部数据
        return menuMapper.selectVoList(queryWrapper);
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
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getName, dto.getName());
        if (this.count(wrapper) > 0) {
            return false;
        }
        Menu menu = BeanCopyUtils.copy(dto, new Menu());
        if(MenuTypeEnum.BUTTON.getCode().equals(menu.getMenuType())) {
            menu.setShowLink(false);
        }
        return this.save(menu);
    }

    @Override
    public Boolean updateMenu(MenuDto dto) {
        Menu menu = BeanCopyUtils.copy(dto, new Menu());
        if(MenuTypeEnum.BUTTON.getCode().equals(menu.getMenuType())) {
            menu.setShowLink(false);
        }
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
            if(!CollectionUtils.isEmpty(routeVoCache.get(key))) {
                List<RouteVo> list = getRouteList(user.getId());
                // routeVoCache.put(key, list);
            }
        });
    }

    /**
     * 获取路由表
     * @param userId 用户id
     * @return 路由表
     */
    private List<RouteVo> getRouteList(Long userId) {
        List<RouteVo> treeList = new ArrayList<>();
        // 查询用户具有的menu_id
        List<Long> menuIds = userRoleMapper.queryMenuIdByUserId(userId);
        if (!CollectionUtils.isEmpty(menuIds)) {
            // 菜单全部数据(当前用户下的)
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getId, menuIds).orderByAsc(Menu::getSort);
            List<Menu> list = menuMapper.selectList(queryWrapper);

            // 构建树形
            for (Menu rootNode : getRootNodes(list)) {
                RouteVo childrenNode = getChildrenNode(rootNode, list);
                RouteVo.Meta meta = new RouteVo.Meta();
                BeanCopyUtils.copy(rootNode, meta);
                childrenNode.setMeta(meta);
                treeList.add(childrenNode);
            }
        }

        return treeList;
    }

    /**
     * 获取父级跟节点
     *
     */
    private List<Menu> getRootNodes(List<Menu> list) {
        List<Menu> rootList = new ArrayList<>();
        for (Menu menu : list) {
            if (menu.getParentId() == null || menu.getParentId().equals(0L)) {
                rootList.add(menu);
            }
        }
        // 根节点排序
        rootList.sort(Comparator.comparingInt(Menu::getSort));
        return rootList;
    }

    private RouteVo getChildrenNode(Menu rootNode, List<Menu> menuList) {
        List<RouteVo> childrenList = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getParentId().equals(rootNode.getId()) && !MenuTypeEnum.BUTTON.getCode().equals(menu.getMenuType())) {
                childrenList.add(getChildrenNode(menu, menuList));
            }
        }
        RouteVo vo = new RouteVo();
        BeanCopyUtils.copy(rootNode, vo);
        vo.setChildren(childrenList);
        RouteVo.Meta meta = new RouteVo.Meta();
        BeanCopyUtils.copy(rootNode, meta);
        getMenuAuths(rootNode.getId(), meta,menuList);
        vo.setMeta(meta);
        return vo;
    }

    private void getMenuAuths(Long parentId, RouteVo.Meta meta, List<Menu> list) {
        List<String> auths = new ArrayList<>();
        for (Menu menu : list) {
            if (parentId.equals(menu.getParentId()) && MenuTypeEnum.BUTTON.getCode().equals(menu.getMenuType())) {
                auths.add(menu.getAuths());
            }
        }
        meta.setAuths(auths.toArray(new String[0]));
    }
}

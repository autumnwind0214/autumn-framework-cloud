package com.autumn.auth.service.impl;

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
import com.autumn.common.core.base.AutumnTreeNode;
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

    private final IAuthorizationUserService authorizationUserService;



    @Override
    public List<DynamicRouteVo> getAsyncRoutes(Long userId) {
        String key = RedisConstant.ASYNC_ROUTES_PREFIX_KEY + userId;
        // List<RouteVo> routeList = routeVoCache.get(key);
        List<DynamicRouteVo> routeList = new ArrayList<>();
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
        return this.save(menu);
    }

    @Override
    public Boolean updateMenu(MenuDto dto) {
        Menu menu = BeanCopyUtils.copy(dto, new Menu());
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
            // if(!CollectionUtils.isEmpty(routeVoCache.get(key))) {
            //     List<RouteVo> list = getRouteList(user.getId());
            //     routeVoCache.put(key, list);
            // }
        });
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
            // 构建树
            List<AutumnTreeNode<DynamicRouteVo>> treeList = treeBuilder.buildTree(
                    routeVoList,
                    DynamicRouteVo::getId,
                    DynamicRouteVo::getParentId,
                    0L
            );
            // 将 AutumnTreeNode 包裹的 data 直接作为对象，并将 children 放到该对象的 children 属性中
            for (AutumnTreeNode<DynamicRouteVo> node : treeList) {
                DynamicRouteVo vo = node.getData();
                vo.setChildren(extractChildren(node.getChildren()));
                routeList.add(vo);
            }
        }

        return routeList;
    }

    // 递归提取子节点数据
    private List<DynamicRouteVo> extractChildren(List<AutumnTreeNode<DynamicRouteVo>> nodes) {
        List<DynamicRouteVo> children = new ArrayList<>();
        if (nodes != null && !nodes.isEmpty()) {
            for (AutumnTreeNode<DynamicRouteVo> node : nodes) {
                DynamicRouteVo vo = node.getData();
                vo.setChildren(extractChildren(node.getChildren()));
                children.add(vo);
            }
        }
        return children;
    }
}

package com.autumn.auth.service.impl;


import cn.hutool.core.lang.Assert;
import com.autumn.auth.entity.Role;
import com.autumn.auth.entity.RoleMenu;
import com.autumn.auth.mapper.RoleMapper;
import com.autumn.auth.model.dto.RoleAuthDto;
import com.autumn.auth.model.dto.RoleDto;
import com.autumn.auth.model.vo.RoleVo;
import com.autumn.auth.service.IRoleMenuService;
import com.autumn.auth.service.IRoleService;
import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.core.utils.MapstructUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author autumn
 * @desc RoleServiceImpl
 * @date 2025年05月02日
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMapper roleMapper;

    private final IRoleMenuService roleMenuService;


    @Override
    public Page<RoleVo> listPage(RoleDto dto) {
        Page<RoleVo> page = new Page<>(dto.getPage(), dto.getSize());
        return roleMapper.listPage(page, dto);
    }

    @Override
    public List<Long> getRoleMenuId(Long roleId) {
        // 获取角色所绑定的菜单
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(RoleMenu::getMenuId).eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> list = roleMenuService.list(queryWrapper);
        return list.stream().map(RoleMenu::getMenuId).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long[] roleIds) {
        List<Long> list = Arrays.asList(roleIds);
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, list));
        return removeByIds(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editAuth(RoleAuthDto dto) {
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, dto.getRoleId()));
        List<RoleMenu> list = new ArrayList<>();
        for (Long permission : dto.getPermission()) {
            RoleMenu roleMenu = new RoleMenu(dto.getRoleId(), permission);
            list.add(roleMenu);
        }
        return roleMenuService.saveBatch(list);
    }

    @Override
    public Boolean add(RoleDto dto) {
        if (checkDuplicateRoles(null, dto.getRoleName())) {
            throw new AutumnException(ResultCodeEnum.DUPLICATE_ROLES);
        }
        Role role = MapstructUtils.convert(dto, Role.class);
        return save(role);
    }

    @Override
    public Boolean edit(RoleDto dto) {
        if (checkDuplicateRoles(dto.getId(), dto.getRoleName())) {
            throw new AutumnException(ResultCodeEnum.DUPLICATE_ROLES);
        }
        Role role = MapstructUtils.convert(dto, Role.class);
        return updateById(role);
    }

    @Override
    public List<RoleVo> getListAll() {
        return roleMapper.selectVoList();
    }

    @Override
    public Boolean editStatus(Long roleId, Integer status) {
        Role role = new Role();
        role.setId(roleId);
        role.setStatus(status);
        return updateById(role);
    }

    /**
     * 检查角色名是否重复
     *
     * @param id   id
     * @param role 角色
     * @return true 表示重复，false 表示不重复
     */
    private boolean checkDuplicateRoles(Long id, String role) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(Role::getRole, role));
        if (id != null) {
            wrapper.and(w -> w.ne(Role::getId, id));
        }
        return count(wrapper) > 0;
    }
}

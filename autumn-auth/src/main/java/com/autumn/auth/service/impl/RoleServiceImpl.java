package com.autumn.auth.service.impl;


import cn.hutool.core.lang.Assert;
import com.autumn.auth.entity.Role;
import com.autumn.auth.entity.RoleMenu;
import com.autumn.auth.mapper.RoleMapper;
import com.autumn.auth.model.dto.RoleDto;
import com.autumn.auth.model.vo.RoleVo;
import com.autumn.auth.service.IRoleMenuService;
import com.autumn.auth.service.IRoleService;
import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.core.utils.I18nUtils;
import com.autumn.common.core.utils.MapstructUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long[] roleIds) {
        List<Long> list = Arrays.asList(roleIds);
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, list));
        return removeByIds(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(RoleDto dto) {
        if (checkDuplicateRoles(null, dto.getRoleName())) {
            throw new AutumnException(ResultCodeEnum.DUPLICATE_ROLES);
        }
        Role role = MapstructUtils.convert(dto, Role.class);
        if (save( role)) {
            // 配置角色权限
            return roleMenuService.setPermission(role, dto.getPermissions());
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(RoleDto dto) {
        if (checkDuplicateRoles(dto.getId(), dto.getRoleName())) {
            throw new AutumnException(ResultCodeEnum.DUPLICATE_ROLES);
        }
        Role role = MapstructUtils.convert(dto, Role.class);
        if (updateById(role)) {
            // 配置角色权限
            return roleMenuService.setPermission(role, dto.getPermissions());
        }
        return false;
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

    @Override
    public RoleVo getRole(Long id) {
        RoleVo roleVo = roleMapper.selectVoById(id);
        Assert.notNull(roleVo, I18nUtils.getMessage(I18nUtils.ROLE_NOT_EXIST, null));
        // 获取角色权限
        List<Long> permissions = roleMenuService.getRolePermissions(roleVo.getId());
        roleVo.setPermissions(permissions);
        return roleVo;
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

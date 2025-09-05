package com.autumn.auth.service.impl;


import com.autumn.auth.entity.Role;
import com.autumn.auth.entity.RoleMenu;
import com.autumn.auth.mapper.RoleMenuMapper;
import com.autumn.auth.service.IRoleMenuService;
import com.autumn.common.core.utils.I18nUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author autumn
 */
@Service
@RequiredArgsConstructor
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setPermission(Role role, Long[] permissions) {
        Assert.notNull(role, I18nUtils.getMessage("ROLE_NOT_EMPTY", null));
        this.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getId()));
        List<RoleMenu> list = new ArrayList<>();
        Arrays.stream(permissions).sorted().forEach(permission -> {
            RoleMenu roleMenu = new RoleMenu(role.getId(), permission);
            list.add(roleMenu);
        });
        return this.saveBatch(list);
    }

    @Override
    public List<Long> getRolePermissions(Long roleId) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(RoleMenu::getMenuId).eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> list = this.list(queryWrapper);
        return list.stream().map(RoleMenu::getMenuId).toList();
    }
}

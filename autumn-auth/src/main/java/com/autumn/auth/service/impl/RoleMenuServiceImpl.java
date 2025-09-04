package com.autumn.auth.service.impl;


import com.autumn.auth.entity.RoleMenu;
import com.autumn.auth.mapper.RoleMenuMapper;
import com.autumn.auth.service.IRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author autumn
 * @desc RoleMenuServiceImpl
 * @date 2025年05月02日
 */
@Service
@RequiredArgsConstructor
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editAuth(Long roleId, Long[] permissions) {
        this.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
        List<RoleMenu> list = new ArrayList<>();
        for (Long permission : permissions) {
            RoleMenu roleMenu = new RoleMenu(roleId, permission);
            list.add(roleMenu);
        }
        return this.saveBatch(list);
    }
}

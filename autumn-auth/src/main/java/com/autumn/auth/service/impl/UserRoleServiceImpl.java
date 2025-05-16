package com.autumn.auth.service.impl;


import com.autumn.auth.entity.UserRole;
import com.autumn.auth.mapper.UserRoleMapper;
import com.autumn.auth.model.dto.UserRoleDto;
import com.autumn.auth.service.IUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author autumn
 * @desc UserRoleServiceImpl
 * @date 2025年05月03日
 */
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    private final UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean putAssignRole(UserRoleDto dto) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, dto.getUserId()));
        List<UserRole> list = new ArrayList<>();
        for (Long roleId : dto.getRoleIds()) {
            // 禁止分配超级管理员角色
            if (roleId == 1L) {
                continue;
            }
            UserRole userRole = new UserRole(dto.getUserId(), roleId);
            list.add(userRole);
        }
        return saveBatch(list);
    }
}

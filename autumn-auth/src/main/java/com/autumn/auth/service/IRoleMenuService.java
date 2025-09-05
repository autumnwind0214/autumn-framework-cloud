package com.autumn.auth.service;

import com.autumn.auth.entity.Role;
import com.autumn.auth.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author autumn
 */
public interface IRoleMenuService extends IService<RoleMenu> {
    Boolean setPermission(Role role, Long[] permissions);

    List<Long> getRolePermissions(Long roleId);
}

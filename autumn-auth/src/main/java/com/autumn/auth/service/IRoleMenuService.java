package com.autumn.auth.service;

import com.autumn.auth.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * @author autumn
 * @desc IRoleMenuService
 * @date 2025年05月02日
 */
public interface IRoleMenuService extends IService<RoleMenu> {
    boolean editAuth(Long roleId, Long[] permissions);
}

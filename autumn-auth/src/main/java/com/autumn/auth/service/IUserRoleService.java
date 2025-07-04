package com.autumn.auth.service;


import com.autumn.auth.entity.UserRole;
import com.autumn.auth.model.dto.UserRoleDto;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author autumn
 * @desc IUserRoleService
 * @date 2025年05月03日
 */
public interface IUserRoleService extends IService<UserRole> {

    Boolean assignRole(UserRoleDto dto);

    String[] codes(Long userId);
}

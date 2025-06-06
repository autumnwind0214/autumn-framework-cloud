package com.autumn.auth.service;


import com.autumn.auth.entity.Role;
import com.autumn.auth.model.dto.RoleAuthDto;
import com.autumn.auth.model.dto.RoleDto;
import com.autumn.auth.model.vo.RoleVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author autumn
 * @desc IRoleService
 * @date 2025年05月02日
 */
public interface IRoleService extends IService<Role> {
    Page<RoleVo> listPage(RoleDto form);

    List<Long> getRoleMenuId(Long roleId);

    Boolean putIsLock(Long roleId, Integer isLock);

    Boolean delete(Long[] roleId);

    Boolean editAuth(RoleAuthDto dto);

    Boolean add(RoleDto dto);

    Boolean edit(RoleDto dto);

    List<RoleVo> getListAll();
}

package com.autumn.auth.service;


import com.autumn.auth.entity.Role;
import com.autumn.auth.model.dto.RoleAuthDto;
import com.autumn.auth.model.dto.RoleDto;
import com.autumn.auth.model.vo.RoleVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author autumn
 */
public interface IRoleService extends IService<Role> {
    Page<RoleVo> listPage(RoleDto form);

    Boolean delete(Long[] roleId);

    Boolean add(RoleDto dto);

    Boolean edit(RoleDto dto);

    List<RoleVo> all();

    Boolean editStatus(Long roleId, Integer status);

    RoleVo getRole(Long id);
}

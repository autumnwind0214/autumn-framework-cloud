package com.autumn.auth.controller;

import cn.hutool.core.util.ArrayUtil;

import com.autumn.auth.model.dto.RoleAuthDto;
import com.autumn.auth.model.dto.RoleDto;
import com.autumn.auth.model.vo.RoleMenuVo;
import com.autumn.auth.model.vo.RoleVo;
import com.autumn.auth.service.IMenuService;
import com.autumn.auth.service.IRoleService;
import com.autumn.common.core.result.R;
import com.autumn.mybatis.group.InsertGroup;
import com.autumn.mybatis.group.UpdateGroup;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author autumn
 */
@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {


    private final IRoleService roleService;

    private final IMenuService menuService;


    @PostMapping("/listPage")
    public Page<RoleVo> listPage(@RequestBody RoleDto dto) {
        return roleService.listPage(dto);
    }

    @GetMapping("/roleMenuId/{roleId}")
    public List<Long> getRoleMenuId(@PathVariable Long roleId) {
        return roleService.getRoleMenuId(roleId);
    }

    @GetMapping("/roleMenus")
    public List<RoleMenuVo> getRoleMenus() {
        return menuService.getRoleMenus();
    }

    @GetMapping("/listAll")
    public List<RoleVo> getListAll() {
        return roleService.getListAll();
    }

    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody RoleDto dto) {
        return roleService.add(dto);
    }

    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping
    public R<Boolean> edit(@Validated(UpdateGroup.class) @RequestBody RoleDto dto) {
        if (Long.valueOf(1L).equals(dto.getId()) && Integer.valueOf(0).equals(dto.getStatus())) {
            return R.fail("禁止修改超级管理员");
        }
        return R.success(roleService.edit(dto));
    }

    // @PreAuthorize("hasAuthority('system:role:status')")
    @PutMapping("/status/{roleId}/{status}")
    public R<Boolean> editStatus(@NotNull @PathVariable Long roleId, @NotNull @PathVariable Integer status) {
        if (Long.valueOf(1L).equals(roleId)) {
            return R.fail("禁止禁用超级管理员");
        }
        return R.success(roleService.editStatus(roleId, status));
    }

    @PreAuthorize("hasAuthority('system:role:delete')")
    @DeleteMapping("{ids}")
    public R<Boolean> delete(@PathVariable Long[] ids) {
        if (ArrayUtil.hasNull(ids)) {
            return R.fail("至少需要提供一个角色ID");
        }
        // 遍历检查超级管理员ID
        if (Arrays.stream(ids).anyMatch(id -> id == 1L)) {
            return R.fail("禁止删除超级管理员");
        }
        return R.success(roleService.delete(ids));
    }

    @PreAuthorize("hasAuthority('system:role:auth')")
    @PutMapping("/auth")
    public R<Boolean> editAuth(@Valid @RequestBody RoleAuthDto dto) {
        if (Long.valueOf(1L).equals(dto.getRoleId())) {
            return R.fail("禁止修改管理员权限");
        }
        return R.success(roleService.editAuth(dto));
    }

}

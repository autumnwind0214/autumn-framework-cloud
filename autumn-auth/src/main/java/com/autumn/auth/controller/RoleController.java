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
 * @desc 角色管理控制器
 * @date 2025年05月02日
 */
@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {


    private final IRoleService roleService;

    private final IMenuService menuService;


    @PostMapping("/listPage")
    public R<Page<RoleVo>> listPage(@RequestBody RoleDto dto) {
        return R.success(roleService.listPage(dto));
    }

    @GetMapping("/roleMenuId/{roleId}")
    public R<List<Long>> getRoleMenuId(@PathVariable Long roleId) {
        return R.success(roleService.getRoleMenuId(roleId));
    }

    @GetMapping("/roleMenus")
    public R<List<RoleMenuVo>> getRoleMenus() {
        return R.success(menuService.getRoleMenus());
    }

    @GetMapping("/listAll")
    public R<List<RoleVo>> getListAll() {
        return R.success(roleService.getListAll());
    }

    @PreAuthorize("hasAuthority('system:role:add')")
    @PostMapping
    public R<Boolean> add(@Validated(InsertGroup.class) @RequestBody RoleDto dto) {
        return R.success(roleService.add(dto));
    }

    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping
    public R<Boolean> edit(@Validated(UpdateGroup.class) @RequestBody RoleDto dto) {
        if (Long.valueOf(1L).equals(dto.getId())) {
            return R.fail("禁止修改超级管理员");
        }
        return R.success(roleService.edit(dto));
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

    @PreAuthorize("hasAuthority('system:role:isLock')")
    @PutMapping("/isLock/{roleId}/{isLock}")
    public R<Boolean> putIsLock(@NotNull @PathVariable Long roleId, @NotNull @PathVariable Integer isLock) {
        if (Long.valueOf(1L).equals(roleId)) {
            return R.fail("禁止锁定超级管理员");
        }
        return R.success(roleService.putIsLock(roleId, isLock));
    }

    @PreAuthorize("hasAuthority('system:role:auth')")
    @PutMapping("/auth")
    public R<Boolean> editAuth(@Valid @RequestBody RoleAuthDto dto) {
        if (Long.valueOf(1L).equals(dto.getRoleId())) {
            // return R.fail("禁止修改管理员权限");
        }
        return R.success(roleService.editAuth(dto));
    }

}

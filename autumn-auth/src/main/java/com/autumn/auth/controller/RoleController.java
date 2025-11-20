package com.autumn.auth.controller;

import com.autumn.auth.model.dto.RoleDto;
import com.autumn.auth.model.vo.RoleMenuVo;
import com.autumn.auth.model.vo.RoleVo;
import com.autumn.auth.service.IMenuService;
import com.autumn.auth.service.IRoleService;
import com.autumn.common.core.result.R;
import com.autumn.common.core.utils.VerifyCheckUtils;
import com.autumn.mybatis.group.InsertGroup;
import com.autumn.mybatis.group.UpdateGroup;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
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


    /**
     * 列表查询
     *
     * @param dto 查询参数
     * @return 角色列表
     */
    @PostMapping("/listPage")
    public Page<RoleVo> listPage(@RequestBody RoleDto dto) {
        return roleService.listPage(dto);
    }

    /**
     * 获取角色菜单
     *
     * @return 角色菜单列表
     */
    @GetMapping("/roleMenus")
    public List<RoleMenuVo> getRoleMenus() {
        return menuService.getRoleMenus();
    }

    /**
     * 获取所有角色
     *
     * @return 角色列表
     */
    @GetMapping("/all")
    public List<RoleVo> all() {
        return roleService.all();
    }

    /**
     * 获取角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    @GetMapping("/{id}")
    public RoleVo get(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    /**
     * 新增角色
     *
     * @param dto 角色信息
     * @return 是否成功
     */
    @PreAuthorize("hasRole('admin') || hasAuthority('system:role:add')")
    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody RoleDto dto) {
        return roleService.add(dto);
    }

    /**
     * 修改角色
     *
     * @param dto 角色信息
     * @return 是否成功
     */
    @PreAuthorize("hasRole('admin') || hasAuthority('system:role:edit')")
    @PutMapping
    public R<Boolean> edit(@Validated(UpdateGroup.class) @RequestBody RoleDto dto) {
        VerifyCheckUtils.checkAdminEdit(dto.getId());
        return R.success(roleService.edit(dto));
    }

    /**
     * 修改角色状态
     *
     * @param roleId  角色ID
     * @param status  状态
     * @return 是否成功
     */
    @PreAuthorize("hasRole('admin') || hasAuthority('system:role:status')")
    @PutMapping("/status/{roleId}/{status}")
    public R<Boolean> editStatus(@NotNull @PathVariable Long roleId, @NotNull @PathVariable Integer status) {
        if (Long.valueOf(1L).equals(roleId)) {
            return R.fail("禁止禁用超级管理员");
        }
        return R.success(roleService.editStatus(roleId, status));
    }

    /**
     * 删除角色
     *
     * @param ids 角色ID
     * @return 是否成功
     */
    @PreAuthorize("hasRole('admin') || hasAuthority('system:role:delete')")
    @DeleteMapping("{ids}")
    public R<Boolean> delete(@PathVariable Long[] ids) {
        Assert.notEmpty(ids, "至少需要提供一个角色ID");
        // 遍历检查超级管理员ID
        if (Arrays.stream(ids).anyMatch(id -> id == 1L)) {
            return R.fail("禁止删除超级管理员");
        }
        return R.success(roleService.delete(ids));
    }

}

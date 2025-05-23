package com.autumn.auth.controller;

import com.autumn.auth.model.dto.*;
import com.autumn.auth.model.vo.AuthorizationUserVo;
import com.autumn.auth.service.IAuthorizationUserService;
import com.autumn.auth.service.IUserRoleService;
import com.autumn.auth.utils.SecurityUtils;
import com.autumn.common.core.annotation.ValidStatus;
import com.autumn.common.core.result.R;
import com.autumn.common.sensitive.annotation.Sensitive;
import com.autumn.mybatis.group.InsertGroup;
import com.autumn.mybatis.group.UpdateGroup;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author autumn
 * @desc 用户管理控制器
 * @date 2025/4/13 14:02
 **/
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthorizationUserController {

    private final IAuthorizationUserService authorizationUserService;

    private final IUserRoleService userRoleService;

    @GetMapping("/{userId}")
    public AuthorizationUserVo getUserInfo(@PathVariable Long userId) {
        return authorizationUserService.getUserInfo(userId);
    }

    @GetMapping("/roleIds/{userId}")
    public Long[] getRoleIds(@PathVariable Long userId) {
        return authorizationUserService.getRoleIds(userId);
    }

    @GetMapping("/mine")
    public AuthorizationUserVo getMine() {
        Long userId = SecurityUtils.getCurrentUserId();
        return authorizationUserService.getUserInfo(userId);
    }

    @Sensitive
    @PostMapping("/listPage")
    public Page<AuthorizationUserVo> listPage(@RequestBody UserInfoDto dto) {
        return authorizationUserService.listPage(dto);
    }

    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public R<Boolean> add(@Validated(InsertGroup.class) @RequestBody UserDto dto) {
        if (!dto.getPassword().equals(dto.getNewPassword())) {
            return R.fail("两次密码输入不一致");
        }
        return R.success(authorizationUserService.add(dto));
    }

    @PreAuthorize("hasAuthority('system:user:assignRole')")
    @PutMapping("/assignRole")
    public R<Boolean> putAssignRole(@Validated @RequestBody UserRoleDto dto) {
        return R.success(userRoleService.putAssignRole(dto));
    }

    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping
    public R<Boolean> edit(@Validated(UpdateGroup.class) @RequestBody UserDto dto) {
        if (dto.getId().equals(1L) && !"admin".equals(dto.getUsername())) {
            return R.fail("禁止修改超级管理员用户名");
        }
        return R.success(authorizationUserService.edit(dto));
    }

    @PreAuthorize("hasAuthority('system:user:status')")
    @PutMapping("/status/{id}/{status}")
    public R<Boolean> editStatus(@PathVariable("id") @NotNull Long id, @PathVariable("status") @NotNull @ValidStatus Integer status) {
        if (id == 1) {
            return R.fail("禁止修改超级管理员状态");
        }
        return R.success(authorizationUserService.editStatus(id, status));
    }

    @PreAuthorize("hasAuthority('system:user:changePassword')")
    @PutMapping("/changePassword")
    public R<Boolean> reset(@Validated @RequestBody ChangePasswordDto dto) {
        if (dto.getUserId() != null && dto.getUserId() == 1) {
            return R.fail("禁止修改超级管理员密码");
        }
        if (!dto.getPassword().equals(dto.getConfirmPwd())) {
            return R.fail("两次密码输入不一致");
        }
        return R.success(authorizationUserService.changePassword(dto));
    }

    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/uploadAvatar")
    public R<Boolean> uploadAvatar(@Validated @RequestBody UserAvatarDto dto) {
        return R.success(authorizationUserService.uploadAvatar(dto));
    }

    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/mine")
    public R<Boolean> putMine(@Validated(UpdateGroup.class) @RequestBody UserDto dto) {
        return R.success(authorizationUserService.edit(dto));
    }

    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/{ids}")
    public R<Boolean> delete(@PathVariable Long[] ids) {
        // 遍历检查超级管理员ID
        if (Arrays.stream(ids).anyMatch(id -> id == 1L)) {
            return R.fail("禁止删除超级管理员");
        }
        return R.success(authorizationUserService.delete(ids));
    }


}

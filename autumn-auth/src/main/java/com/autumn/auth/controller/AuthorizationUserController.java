package com.autumn.auth.controller;

import com.autumn.auth.model.dto.*;
import com.autumn.auth.model.vo.AuthorizationUserVo;
import com.autumn.auth.service.IAuthorizationUserService;
import com.autumn.auth.service.IUserRoleService;
import com.autumn.auth.utils.SecurityUtils;
import com.autumn.common.core.annotation.ValidStatus;
import com.autumn.common.core.result.R;
import com.autumn.common.core.utils.VerifyCheckUtils;
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
 * 用户管理控制器
 *
 * @author autumn
 **/
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthorizationUserController {

    private final IAuthorizationUserService authorizationUserService;

    private final IUserRoleService userRoleService;

    /**
     * 获取用户信息
     */
    @GetMapping("/{userId}")
    public AuthorizationUserVo getUserInfo(@PathVariable Long userId) {
        return authorizationUserService.getUserInfo(userId);
    }

    /**
     * 获取用户角色
     */
    @GetMapping("/roleIds/{userId}")
    public Long[] getRoleIds(@PathVariable Long userId) {
        return authorizationUserService.getRoleIds(userId);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/mine")
    public AuthorizationUserVo getMine() {
        Long userId = SecurityUtils.getCurrentUserId();
        return authorizationUserService.getUserInfo(userId);
    }

    /**
     * 获取当前登录用户权限
     */
    @GetMapping("/codes")
    public String[] getCodes() {
        Long userId = SecurityUtils.getCurrentUserId();
        return userRoleService.codes(userId);
    }

    /**
     * 获取用户列表
     */
    @Sensitive
    @PostMapping("/listPage")
    public Page<AuthorizationUserVo> listPage(@RequestBody UserInfoDto dto) {
        return authorizationUserService.listPage(dto);
    }

    /**
     * 新增用户
     */
    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody UserDto dto) {
        VerifyCheckUtils.checkPassword(dto.getPassword(), dto.getNewPassword());
        return authorizationUserService.add(dto);
    }

    /**
     * 分配角色
     */
    @PreAuthorize("hasAuthority('system:user:assignRole')")
    @PutMapping("/assignRole")
    public R<Boolean> putAssignRole(@Validated @RequestBody UserRoleDto dto) {
        return R.success(userRoleService.assignRole(dto));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody UserDto dto) {
        VerifyCheckUtils.checkAdminEdit(dto.getId());
        return authorizationUserService.edit(dto);
    }

    /**
     * 禁用
     */
    @PreAuthorize("hasAuthority('system:user:disabled')")
    @PutMapping("/disabled/{id}/{disabled}")
    public Boolean disabled(@PathVariable("id") @NotNull Long id,
                            @PathVariable("disabled")
                            @NotNull @ValidStatus Integer disabled) {
        VerifyCheckUtils.checkAdminEdit(id);
        return authorizationUserService.disabled(id, disabled);
    }

    /**
     * 解锁
     */
    @PreAuthorize("hasAuthority('system:user:unlock')")
    @PutMapping("/unlock/{userId}")
    public Boolean unlock(@PathVariable("userId") @NotNull Long userId) {
        return authorizationUserService.unlock(userId);
    }

    /**
     * 修改密码
     */
    @PreAuthorize("hasAuthority('system:user:changePassword')")
    @PutMapping("/changePassword")
    public Boolean reset(@Validated @RequestBody ChangePasswordDto dto) {
        VerifyCheckUtils.checkAdminEdit(dto.getUserId());
        VerifyCheckUtils.checkPassword(dto.getPassword(), dto.getConfirmPwd());
        return authorizationUserService.changePassword(dto);
    }

    /**
     * 上传头像
     */
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/uploadAvatar")
    public Boolean uploadAvatar(@Validated @RequestBody UserAvatarDto dto) {
        return authorizationUserService.uploadAvatar(dto);
    }

    /**
     * 修改当前登录用户信息
     */
    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/mine")
    public Boolean putMine(@Validated(UpdateGroup.class) @RequestBody UserDto dto) {
        return authorizationUserService.edit(dto);
    }

    /**
     * 删除用户
     */
    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable Long[] ids) {
        ids = Arrays.stream(ids)
                .filter(id -> id != 1L)
                .toArray(Long[]::new);
        return authorizationUserService.delete(ids);
    }


}

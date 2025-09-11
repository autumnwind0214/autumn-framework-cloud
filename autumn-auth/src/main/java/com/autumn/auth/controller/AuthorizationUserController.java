package com.autumn.auth.controller;

import com.autumn.auth.model.dto.*;
import com.autumn.auth.model.vo.AuthorizationUserVo;
import com.autumn.auth.service.IAuthorizationUserService;
import com.autumn.auth.service.IUserRoleService;
import com.autumn.auth.utils.SecurityUtils;
import com.autumn.common.core.annotation.ValidStatus;
import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.R;
import com.autumn.common.core.result.ResultCodeEnum;
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

    @GetMapping("/codes")
    public String[] getCodes() {
        Long userId = SecurityUtils.getCurrentUserId();
        return userRoleService.codes(userId);
    }

    @Sensitive
    @PostMapping("/listPage")
    public Page<AuthorizationUserVo> listPage(@RequestBody UserInfoDto dto) {
        return authorizationUserService.listPage(dto);
    }

    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody UserDto dto) {
        if (!dto.getPassword().equals(dto.getNewPassword())) {
            throw new AutumnException(ResultCodeEnum.PASSWORD_NOT_EQUALS);
        }
        return authorizationUserService.add(dto);
    }

    @PreAuthorize("hasAuthority('system:user:assignRole')")
    @PutMapping("/assignRole")
    public R<Boolean> putAssignRole(@Validated @RequestBody UserRoleDto dto) {
        return R.success(userRoleService.assignRole(dto));
    }

    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody UserDto dto) {
        if (dto.getId().equals(1L) && !"admin".equals(dto.getUsername())) {
            throw new AutumnException(ResultCodeEnum.BAN_OPERATION_USER);
        }
        return authorizationUserService.edit(dto);
    }

    /**
     * 禁用
     */
    @PreAuthorize("hasAuthority('system:user:disabled')")
    @PutMapping("/disabled/{id}/{disabled}")
    public Boolean disabled(@PathVariable("id") @NotNull Long id, @PathVariable("disabled") @NotNull @ValidStatus Integer disabled) {
        if (id == 1) {
            throw new AutumnException(ResultCodeEnum.BAN_OPERATION_USER);
        }
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

    @PreAuthorize("hasAuthority('system:user:changePassword')")
    @PutMapping("/changePassword")
    public Boolean reset(@Validated @RequestBody ChangePasswordDto dto) {
        if (dto.getUserId() != null && dto.getUserId() == 1) {
            throw new AutumnException(ResultCodeEnum.BAN_OPERATION_USER);
        }
        if (!dto.getPassword().equals(dto.getConfirmPwd())) {
            throw new AutumnException(ResultCodeEnum.PASSWORD_NOT_EQUALS);
        }
        return authorizationUserService.changePassword(dto);
    }

    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/uploadAvatar")
    public Boolean uploadAvatar(@Validated @RequestBody UserAvatarDto dto) {
        return authorizationUserService.uploadAvatar(dto);
    }

    @PreAuthorize("hasAuthority('system:user:edit')")
    @PutMapping("/mine")
    public Boolean putMine(@Validated(UpdateGroup.class) @RequestBody UserDto dto) {
        return authorizationUserService.edit(dto);
    }

    @PreAuthorize("hasAuthority('system:user:delete')")
    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable Long[] ids) {
        ids = Arrays.stream(ids)
                .filter(id -> id != 1L)
                .toArray(Long[]::new);
        return authorizationUserService.delete(ids);
    }


}

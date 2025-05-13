package com.autumn.auth.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author autumn
 * @desc 分配角色
 * @date 2025年05月03日
 */
@Data
public class UserRoleDto {

    @NotNull(message = "用户id不能为null")
    Long userId;

    Long[] roleIds;
}

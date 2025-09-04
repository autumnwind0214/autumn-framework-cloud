package com.autumn.auth.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author autumn
 * @desc 角色权限
 * @date 2025年05月02日
 */
@Data
public class RoleAuthDto {

    @NotNull(message = "角色id不能为null")
    Long roleId;

    Long[] permissions;
}

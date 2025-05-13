package com.autumn.auth.model.vo;

import com.autumn.auth.entity.Role;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author autumn
 * @desc 角色列表视图
 * @date 2025年05月02日
 */
@Data
@AutoMapper(target = Role.class)
public class RoleVo {

    Long id;

    String roleName;

    String permission;

    Integer isLock;

    LocalDateTime createTime;

    LocalDateTime updateTime;
}

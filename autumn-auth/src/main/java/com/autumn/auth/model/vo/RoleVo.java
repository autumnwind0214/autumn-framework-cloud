package com.autumn.auth.model.vo;

import com.autumn.auth.entity.Role;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色列表视图
 *
 * @author autumn
 */
@Data
@AutoMapper(target = Role.class)
public class RoleVo {

    Long id;

    String roleName;

    String permission;

    Integer status;

    String remark;

    LocalDateTime createTime;

    LocalDateTime updateTime;
}

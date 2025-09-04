package com.autumn.auth.model.vo;

import com.autumn.auth.entity.Role;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    String role;

    Integer status;

    String remark;

    List<Long> permissions;

    LocalDateTime createTime;

    LocalDateTime updateTime;
}

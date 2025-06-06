package com.autumn.auth.entity;

import com.autumn.auth.model.dto.RoleDto;
import com.autumn.auth.model.vo.RoleVo;
import com.baomidou.mybatisplus.annotation.*;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author autumn
 * @desc 角色表
 * @date 2025年05月02日
 */
@AutoMappers({
        @AutoMapper(target = RoleDto.class),
        @AutoMapper(target = RoleVo.class)
})
@Data
@TableName("role")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    Long id;

    String roleName;

    Integer isLock;

    String permission;

    @TableField(fill = FieldFill.INSERT)
    LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    LocalDateTime updateTime;
}

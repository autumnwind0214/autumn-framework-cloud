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
 * 角色表
 *
 * @author autumn
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

    Integer status;

    String role;

    @TableField(fill = FieldFill.INSERT)
    LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    LocalDateTime updateTime;
}

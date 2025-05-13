package com.autumn.auth.model.dto;



import com.autumn.auth.entity.Role;
import com.autumn.mybatis.core.model.PageQuery;
import com.autumn.mybatis.group.InsertGroup;
import com.autumn.mybatis.group.UpdateGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author autumn
 * @desc 角色查询条件
 * @date 2025年05月02日
 */
@AutoMapper(target = Role.class)
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDto extends PageQuery {

    @NotNull(groups = UpdateGroup.class, message = "编辑时 ID 不能为空")
    @Null(groups = InsertGroup.class, message = "新增时 ID 必须为空")
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class}, message = "请输入角色名称")
    private String roleName;

    /**
     * 角色标识
     */
    @NotBlank(groups = {UpdateGroup.class, InsertGroup.class}, message = "请输入角色标识")
    private String permission;

    /**
     * 是否锁定
     */
    private Integer isLock;
}

package com.autumn.auth.model.dto;

import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.mybatis.group.InsertGroup;
import com.autumn.mybatis.group.UpdateGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author autumn
 * @desc 用户新增/修改数据传输对象
 * @date 2025/4/20 22:02
 **/
@Data
@AutoMapper(target = AuthorizationUser.class)
public class UserDto {

    // 新增时允许 id 为空，编辑时要求 id 非空
    @Null(groups = InsertGroup.class, message = "新增时 ID 必须为空")
    @NotNull(groups = UpdateGroup.class, message = "编辑时 ID 不能为空")
    private Long id;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 用户名
     */
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(groups = InsertGroup.class, message = "密码不能为空")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(groups = InsertGroup.class, message = "确认密码不能为空")
    private String newPassword;

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 昵称
     */
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class}, message = "昵称不能为空")
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 状态
     */
    private Boolean status;

    /**
     * 个人简介
     */
    private String remark;

}

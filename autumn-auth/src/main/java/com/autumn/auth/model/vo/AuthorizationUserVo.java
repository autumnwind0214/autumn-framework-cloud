package com.autumn.auth.model.vo;

import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.common.sensitive.annotation.SensitiveData;
import com.autumn.common.sensitive.enums.SensitiveTypeEnum;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author autumn
 * @desc 用户信息
 * @date 2025年05月13日
 */
@Data
@AutoMapper(target = AuthorizationUser.class)
public class AuthorizationUserVo {

    Long id;

    /**
     * 用户名
     */
    String username;

    /**
     * 手机号
     */
    @SensitiveData(type = SensitiveTypeEnum.PHONE)
    String phone;

    /**
     * 昵称
     */
    String nickname;

    /**
     * 邮箱
     */
    @SensitiveData(type = SensitiveTypeEnum.EMAIL)
    String email;

    /**
     * 头像地址
     */
    String avatar;

    /**
     * 生日
     */
    LocalDateTime birthday;

    /**
     * 性别
     */
    Integer sex;

    /**
     * 最后登录时间
     */
    LocalDateTime lastLoginTime;

    // 用户绑定角色
    String[] roles;

    // 用户权限
    String[] permissions;

    Integer status;

}

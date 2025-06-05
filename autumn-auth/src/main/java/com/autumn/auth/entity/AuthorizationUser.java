package com.autumn.auth.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.autumn.auth.model.vo.AuthorizationUserVo;
import com.autumn.mybatis.core.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author autumn
 * @desc 基本用户
 * @date 2025年05月12日
 */
@Getter
@Setter
@JsonSerialize
@TableName("authorization_user")
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMappers({
        @AutoMapper(target = AuthorizationUserVo.class)
})
public class AuthorizationUser extends BaseEntity {
    /**
     * 账户
     */
    String username;

    /**
     * 密码
     */
    String password;

    /**
     * 昵称
     */
    String nickname;

    /**
     * 邮箱
     */
    String email;

    /**
     * 头像
     */
    String avatar;

    /**
     * 性别
     */
    Integer sex;

    /**
     * 手机号
     */
    String mobile;

    /**
     * 个人简介
     */
    String remark;

    /**
     * 账户过期时间
     */
    LocalDateTime accountExpire;

    /**
     * 账户是否被锁定
     * 0: 已锁定    1: 未锁定
     */
    Integer locked;

    /**
     * 用户凭据过期时间
     */
    LocalDateTime credentialExpire;

    /**
     * 生日
     */
    @JSONField(format = "yyyy-MM-dd")
    LocalDateTime birthday;

    /**
     * 登录时间
     */
    LocalDateTime loginTime;

    /**
     * 启用状态
     * 0: 未启用    1: 已启用
     */
    Integer status;

    /**
     * 获取用户凭据是否过期
     * true: 过期    false: 未过期
     */
    public boolean isCredentialsNonExpired() {
        return LocalDateTime.now().isAfter(this.credentialExpire);
    }

    /**
     * 指示是否已启用此用户。禁用的用户不能身份验证
     *
     * @return true: 禁用    false: 启用
     */
    public boolean isEnabled() {
        return status != 1;
    }

    /**
     * 指示是否已过期此用户。过期的用户不能身份验证
     * true: 过期    false: 未过期
     */
    public boolean isAccountNonExpired() {
        return LocalDateTime.now().isAfter(this.accountExpire);
    }

    /**
     * 指示是否已锁定此用户。锁定的用户不能身份验证
     * true: 锁定    false: 未锁定
     */
    public boolean isAccountNonLocked() {
        return status != 1;
    }
}

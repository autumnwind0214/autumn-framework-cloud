package com.autumn.auth.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.autumn.auth.model.vo.AuthorizationUserVo;
import com.autumn.mybatis.core.model.BaseEntity;
import com.autumn.mybatis.handler.BooleanTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 基本用户
 *
 * @author autumn
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
     * 禁用状态
     * 0: 禁用    1: 未禁用
     */
    Integer disabled;

    /**
     * 锁定状态
     * 0: 锁定    1: 未锁定
     */
    Integer locked;

    /**
     * 获取用户凭据是否过期
     * true: 过期    false: 未过期
     */
    public boolean isCredentialsNonExpired() {
        return LocalDateTime.now().isAfter(this.credentialExpire);
    }

    /**
     * 指示是否已启用此用户。禁用的用户不能身份验证
     * 永久性：
     * 1.用户违规操作
     * 2.出于安全考虑管理员禁用账户
     * @return true: 禁用    false: 启用
     */
    public boolean isEnabled() {
        return disabled != 1;
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
     * 临时性：
     * 1.多次登录失败触发安全机制
     * 2.系统检测到异常登录行为
     * true: 锁定    false: 未锁定
     */
    public boolean isAccountNonLocked() {
        return locked != 1;
    }
}

package com.autumn.auth.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.autumn.auth.model.vo.AuthorizationUserVo;
import com.autumn.common.core.utils.AESCryptUtil;
import com.autumn.mybatis.core.model.BaseEntity;
import com.autumn.mybatis.handler.BooleanTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

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
public class AuthorizationUser extends BaseEntity implements UserDetails  {
    /**
     * 账户
     */
    String account;

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
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean locked;

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
    @TableField(typeHandler = BooleanTypeHandler.class)
    Boolean status;

    /**
     * 权限信息
     * 非数据库字段
     */
    @TableField(exist = false)
    private Collection<? extends GrantedAuthority> authorities;


    @Override
    public boolean isCredentialsNonExpired() {
        return LocalDateTime.now().isBefore(this.credentialExpire);
    }

    /**
     * 指示是否已启用此用户。禁用的用户不能身份验证
     *
     * @return true: 已启用    false: 未启用
     */
    @Override
    public boolean isEnabled() {
        return status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return String.valueOf(getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return LocalDateTime.now().isBefore(this.accountExpire);
    }

    /**
     * 指示是否已锁定此用户。锁定的用户不能身份验证
     *
     */
    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }
}

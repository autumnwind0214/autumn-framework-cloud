package com.autumn.auth.model.vo;

import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.common.sensitive.annotation.SensitiveData;
import com.autumn.common.sensitive.enums.SensitiveTypeEnum;
import com.autumn.mybatis.handler.BooleanTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author autumn
 * @desc 用户信息
 * @date 2025年05月13日
 */
@Data
@AutoMapper(target = AuthorizationUser.class)
public class AuthorizationUserVo implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;


    Long id;

    /**
     * 账号
     */
    String username;

    /**
     * 密码
     */
    String password;

    /**
     * 手机号
     */
    @SensitiveData(type = SensitiveTypeEnum.PHONE)
    String mobile;

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
    LocalDateTime loginTime;

    // 用户绑定角色
    String[] roles;

    // 用户权限
    String[] permissions;

    Boolean status;

    String remark;

    /**
     * 权限信息
     * 非数据库字段
     */
    Collection<? extends GrantedAuthority> authorities;

    /**
     * 账户过期时间
     */
    LocalDateTime accountExpire;

    /**
     * 账户是否被锁定
     * 0: 已锁定    1: 未锁定
     */
    Boolean locked;

    /**
     * 用户凭据过期时间
     */
    LocalDateTime credentialExpire;

    List<Long> roleIds;

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
    public String getPassword() {
        return this.password;
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

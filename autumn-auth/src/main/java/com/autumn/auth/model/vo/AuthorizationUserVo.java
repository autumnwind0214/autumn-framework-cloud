package com.autumn.auth.model.vo;

import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.common.sensitive.annotation.SensitiveData;
import com.autumn.common.sensitive.enums.SensitiveTypeEnum;
import com.autumn.mybatis.handler.BooleanTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AutoMapper(target = AuthorizationUser.class)
public class AuthorizationUserVo implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;


    Long id;

    /**
     * 用户名
     */
    String username;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    LocalDateTime loginTime;

    /**
     * 有效期
     */
    LocalDateTime accountExpire;

    // 用户绑定角色
    String[] roles;

    // 用户权限
    String[] permissions;

    Integer disabled;

    Integer locked;

    String remark;

    Long[] roleIds;



}

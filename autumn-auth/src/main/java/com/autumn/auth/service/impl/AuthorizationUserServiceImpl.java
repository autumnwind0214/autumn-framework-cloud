package com.autumn.auth.service.impl;

import com.autumn.auth.constant.SecurityConstants;
import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.auth.local.GrantThreadLocal;
import com.autumn.auth.mapper.AuthorizationUserMapper;
import com.autumn.auth.mapper.UserRoleMapper;
import com.autumn.auth.model.auth.CustomGrantedAuthority;
import com.autumn.auth.model.dto.ChangePasswordDto;
import com.autumn.auth.model.dto.UserAvatarDto;
import com.autumn.auth.model.dto.UserDto;
import com.autumn.auth.model.dto.UserInfoDto;
import com.autumn.auth.model.vo.AuthorizationUserVo;
import com.autumn.auth.service.IAuthorizationUserService;
import com.autumn.common.core.exception.AutumnException;
import com.autumn.common.core.result.ResultCodeEnum;
import com.autumn.common.core.utils.EncryptUtils;
import com.autumn.common.core.utils.MapstructUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author autumn
 * @desc 基础用户信息表 服务实现类
 * @date 2025年05月12日
 */
@Service
@RequiredArgsConstructor
public class AuthorizationUserServiceImpl extends ServiceImpl<AuthorizationUserMapper, AuthorizationUser> implements IAuthorizationUserService {

    private final AuthorizationUserMapper authorizationUserMapper;

    private final UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<AuthorizationUser> wrapper = new LambdaQueryWrapper<>();
        String grantType = GrantThreadLocal.getGrantType();
        switch (grantType) {
            case SecurityConstants.EMAIL_LOGIN_TYPE -> {
                wrapper.eq(AuthorizationUser::getEmail, username);
            }
            case SecurityConstants.SMS_LOGIN_TYPE -> {
                wrapper.eq(AuthorizationUser::getMobile, username);
            }
            default -> {
                wrapper.eq(AuthorizationUser::getUsername, username);
            }
        }
        AuthorizationUser authorizationUser = authorizationUserMapper.selectOne(wrapper);
        if (authorizationUser == null) {
            throw new UsernameNotFoundException(ResultCodeEnum.ACCOUNT_NOT_EXIST.getMessage());
        }

        // 更新登录时间
        authorizationUser.setLoginTime(LocalDateTime.now());
        authorizationUserMapper.update(new LambdaUpdateWrapper<AuthorizationUser>()
                .set(AuthorizationUser::getLoginTime, LocalDateTime.now())
                .eq(AuthorizationUser::getId, authorizationUser.getId()));
        // 用户角色权限
        List<String> rolePermission = userRoleMapper.queryRolesByUserId(authorizationUser.getId());
        // 用户权限
        Collection<CustomGrantedAuthority> authorities = new ArrayList<>();
        userRoleMapper.queryPermissionByUserId(authorizationUser.getId()).forEach(item -> {
            authorities.add(new CustomGrantedAuthority(item));
        });
        rolePermission.forEach(item -> {
            authorities.add(new CustomGrantedAuthority("ROLE_" + item));
        });

        return User.withUsername(String.valueOf(authorizationUser.getId()))
                .password(authorizationUser.getPassword())
                // true 禁用 false  启用
                .disabled(authorizationUser.isEnabled())
                // true 锁定 false  解锁
                .accountLocked(authorizationUser.isAccountNonLocked())
                // true 过期 false  不过期
                .accountExpired(authorizationUser.isAccountNonExpired())
                // true 凭证过期 false  不过期
                .credentialsExpired(authorizationUser.isCredentialsNonExpired())
                .authorities(authorities)
                .build();
    }

    @Override
    public AuthorizationUserVo getUserInfo(Long userId) {
        return authorizationUserMapper.selectVoById(userId);
    }

    @Override
    public Page<AuthorizationUserVo> listPage(UserInfoDto dto) {
        Page<AuthorizationUserVo> page = new Page<>(dto.getPage(), dto.getSize());
        return authorizationUserMapper.listPage(page, dto);
    }

    @Override
    public Boolean add(UserDto dto) {
        // 用户昵称和用户名不能重复
        if (checkDuplicateAccounts(dto)) {
            throw new AutumnException(ResultCodeEnum.DUPLICATE_ACCOUNTS);
        }
        AuthorizationUser authorizationUser = MapstructUtils.convert(dto, AuthorizationUser.class);
        Assert.notNull(authorizationUser, ResultCodeEnum.OBJECT_NOTNULL.getMessage());
        if (authorizationUser.getAccountExpire() == null) {
            authorizationUser.setAccountExpire(LocalDateTime.now().plusWeeks(1));
        }
        authorizationUser.setCredentialExpire(authorizationUser.getAccountExpire());
        authorizationUser.setPassword(new BCryptPasswordEncoder().encode(EncryptUtils.decrypt(authorizationUser.getPassword())));
        authorizationUser.setCreateTime(LocalDateTime.now());
        return save(authorizationUser);
    }

    @Override
    public Boolean edit(UserDto dto) {
        // 用户昵称和用户名不能重复
        if (checkDuplicateAccounts(dto)) {
            throw new AutumnException(ResultCodeEnum.DUPLICATE_ACCOUNTS);
        }
        AuthorizationUser authorizationUser = MapstructUtils.convert(dto, AuthorizationUser.class);
        Assert.notNull(authorizationUser, ResultCodeEnum.OBJECT_NOTNULL.getMessage());
        if (authorizationUser.getAccountExpire() == null) {
            authorizationUser.setAccountExpire(LocalDateTime.now().plusWeeks(1));
        }
        authorizationUser.setCredentialExpire(authorizationUser.getAccountExpire());
        authorizationUser.setPassword(null);
        authorizationUser.setUpdateTime(LocalDateTime.now());
        return updateById(authorizationUser);
    }

    @Override
    public Boolean editStatus(Long id, Integer status) {
        AuthorizationUser user = new AuthorizationUser();
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        user.setId(id);
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long[] ids) {
        return removeBatchByIds(Arrays.asList(ids));
    }

    @Override
    public Boolean changePassword(ChangePasswordDto dto) {
        String passwd = EncryptUtils.decrypt(dto.getPassword());
        AuthorizationUser user = new AuthorizationUser();
        user.setId(dto.getUserId());
        user.setPassword(new BCryptPasswordEncoder().encode(passwd));
        return updateById(user);
    }

    @Override
    public Long[] getRoleIds(Long userId) {
        return userRoleMapper.queryRoleIdsByUserId(userId).toArray(new Long[0]);
    }

    @Override
    public Boolean uploadAvatar(UserAvatarDto dto) {
        AuthorizationUser user = new AuthorizationUser();
        user.setId(dto.getUserId());
        user.setAvatar(dto.getReviewUrl());
        return updateById(user);
    }

    /**
     * 检查用户名或昵称是否重复
     *
     * @return true 表示重复，false 表示不重复
     */
    private boolean checkDuplicateAccounts(UserDto dto) {
        LambdaQueryWrapper<AuthorizationUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w
                .eq(AuthorizationUser::getUsername, dto.getUsername())
                .or()
                .eq(AuthorizationUser::getNickname, dto.getNickname())
                .or()
                .eq(AuthorizationUser::getMobile, dto.getMobile())
                .or()
                .eq(AuthorizationUser::getEmail, dto.getEmail())
        );
        if (dto.getId() != null) {
            wrapper.and(w -> w.ne(AuthorizationUser::getId, dto.getId()));
        }
        return count(wrapper) > 0;
    }
}

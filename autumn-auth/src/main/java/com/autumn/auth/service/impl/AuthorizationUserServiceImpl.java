package com.autumn.auth.service.impl;

import com.autumn.auth.entity.AuthorizationUser;
import com.autumn.auth.mapper.AuthorizationUserMapper;
import com.autumn.auth.service.IAuthorizationUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author autumn
 * @desc 基础用户信息表 服务实现类
 * @date 2025年05月12日
 */
@Service
@RequiredArgsConstructor
public class AuthorizationUserServiceImpl extends ServiceImpl<AuthorizationUserMapper, AuthorizationUser> implements IAuthorizationUserService {

    private final AuthorizationUserMapper authorizationUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<AuthorizationUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.or().eq(AuthorizationUser::getUsername, username)
                .or().eq(AuthorizationUser::getMobile, username)
                .or().eq(AuthorizationUser::getEmail, username);
        return authorizationUserMapper.selectOne(wrapper);
    }
}

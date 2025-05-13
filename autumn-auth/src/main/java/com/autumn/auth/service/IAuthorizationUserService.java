package com.autumn.auth.service;

import com.autumn.auth.entity.AuthorizationUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author autumn
 * @desc IAuthorizationUserService
 * @date 2025年05月12日
 */
public interface IAuthorizationUserService extends IService<AuthorizationUser> , UserDetailsService {
}

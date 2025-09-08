package com.autumn.auth.config;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

/**
 * 自定义权限管理器，用于处理admin角色的特殊权限
 * admin角色拥有所有权限，可以访问所有接口
 */
public class AdminAuthorityAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication.get();

        // 如果用户未认证，拒绝访问
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        // 检查用户是否具有admin角色
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if ("ROLE_admin".equals(authority.getAuthority())) {
                // admin角色拥有所有权限
                return new AuthorizationDecision(true);
            }
        }

        // 非admin角色按原有逻辑处理（返回null表示使用默认的权限检查机制）
        return null;
    }
}
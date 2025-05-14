package com.autumn.common.auth.config;

import com.autumn.common.auth.constant.GrantAuthConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author autumn
 * @desc 解析用户权限信息（当在浏览器中直接访问接口，框架自动调用OIDC流程登录时会用到该配置）
 * @date 2025年05月13日
 */
@Component
public class AuthoritiesConfig {

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            authorities.forEach(authority -> {
                if (authority instanceof OAuth2UserAuthority oAuth2UserAuthority) {
                    // 从认证服务获取的用户信息中提取权限信息
                    Object userAuthorities = oAuth2UserAuthority.getAttributes().get(GrantAuthConstant.AUTHORITIES_KEY);
                    if (userAuthorities instanceof Collection<?> collection) {
                        // 转为SimpleGrantedAuthority的实例并插入mappedAuthorities中
                        collection.stream().filter(a -> a instanceof String)
                                .map(String::valueOf)
                                .map(SimpleGrantedAuthority::new)
                                .forEach(mappedAuthorities::add);
                    }
                }
            });
            return mappedAuthorities;
        };
    }
}

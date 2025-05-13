package com.autumn.auth.model.vo;

import lombok.Data;

import java.time.Instant;
import java.util.Set;

/**
 * @author autumn
 * @desc 授权令牌返回值
 * @date 2025/5/12 22:14
 **/
@Data
public class AccessTokenVo {

    /**
     * 授权令牌
     */
    private String accessToken;

    /**
     * 授权范围
     */
    private Set<String> scope;

    /**
     * 授权类型
     */
    private String tokenType;

    /**
     * 过期时间
     */
    private Instant expiresAt;

    /**
     * 创建时间
     */
    private Instant issuedAt;

    /**
     * 刷新令牌
     */
    private RefreshTokenVo refreshToken;
}
